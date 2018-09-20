package org.satran.blockchain.graphql;

import org.aion.api.IAionAPI;
import org.aion.api.IContract;
import org.aion.api.IUtils;
import org.aion.api.sol.IAddress;
import org.aion.api.sol.ISString;
import org.aion.api.type.*;
import org.aion.base.type.Address;
import org.aion.base.type.Hash256;
import org.aion.base.util.ByteArrayWrapper;
import org.aion.base.util.Bytesable;

import java.math.BigInteger;
import java.util.Map;

import static org.aion.api.ITx.*;

public class ContractExample {

    public static void main(String[] args) throws InterruptedException {

        // connect to Java API
        IAionAPI api = IAionAPI.init();
        ApiMsg apiMsg = api.connect("tcp://localhost:8548");

        // failed connection
        if (apiMsg.isError()) {
            System.out.format("Could not connect due to <%s>%n", apiMsg.getErrString());
            System.exit(-1);
        }

        // 1. eth_getCompilers

        // not available/needed at present

        // 2. eth_compileSolidity

        // contract source code
        String source_ticker = "contract ticker { uint public val; function tick () { val+= 1; } }";

        // compile code
        Map<String, CompileResponse> result = api.getTx().compile(source_ticker).getObject();

        // print result
        System.out.println(result);

        // 3. eth_gasPrice

        // get NRG price
        long price = api.getTx().getNrgPrice().getObject();

        // print price
        System.out.println("\ncurrent NRG price = " + price + " nAmp");

        // 4. eth_estimateGas

        // get NRG estimate for contract
        long estimate = api.getTx().estimateNrg(result.get("ticker").getCode()).getObject();

        // print estimate
        System.out.println("\nNRG estimate for contract = " + estimate + " NRG");

        // transaction data
        Address sender = Address.wrap("a06f02e986965ddd3398c4de87e3708072ad58d96e9c53e87c31c8c970b211e5");
        Address receiver = Address.wrap("a0bd0ef93902d9e123521a67bef7391e9487e963b2346ef3b3ff78208835545e");
        BigInteger amount = BigInteger.valueOf(1_000_000_000_000_000_000L); // = 1 AION
        ByteArrayWrapper data = ByteArrayWrapper.wrap("test.message".getBytes());

        // prepare transaction
        TxArgs tx = new TxArgs.TxArgsBuilder().data(data).from(sender).to(receiver).value(amount).createTxArgs();

        // get NRG estimate for transaction
        estimate = api.getTx().estimateNrg(tx).getObject();

        // print estimate
        System.out.println("\nNRG estimate for transaction = " + estimate + " NRG");

        // 5. eth_getCode

        // set contract account
        Address contractAccount = Address.wrap("a0960fcb7d6423a0446243916c7c6360543b3d2f9c5e1c5ff7badb472b782b79");

        // get code from latest block
        long blockNumber = -1L; // indicates latest block
        byte[] code = api.getTx().getCode(contractAccount, blockNumber).getObject();

        // print code
        System.out.println("\n0x" + IUtils.bytes2Hex(code));

        // 6. eth_getStorageAt

        // view contract creation tx
      /*  Hash256 txHash = Hash256.wrap("0xb42a5f995450531f66e7db40efdfad2c310fa0f8dbca2a88c31fdc4837368e48");
        TxReceipt receipt = api.getTx().getTxReceipt(txHash).getObject();
        System.out.println("\n" + receipt);

        // set contract account
        contractAccount = receipt.getContractAddress();

        // get value from storage
        String valuePos0 = api.getChain().getStorageAt(contractAccount, 0, blockNumber).getObject();
        String valuePos1 = api.getChain().getStorageAt(contractAccount, 1, blockNumber).getObject();

        // print values
        // in this case the first two values are the contract owner
        System.out.println("concatenated values = " + valuePos0 + valuePos1);*/

        // 7.a) deploy contract

        // contract source code
        String source_personnel =
                "contract Personnel { address public owner; modifier onlyOwner() { require(msg.sender == owner); _;} "
                        + "mapping(bytes32 => address) private userList; /** 3 LSB bits for each privilege type */ "
                        + "mapping(address => bytes1) private userPrivilege; function Personnel(){ owner = msg.sender; } "
                        + "event UserAdded(string _stamp); event AddressAdded(address indexed _addr); "
                        + "function getUserAddress(string _stamp) constant returns (address){ return userList[sha3(_stamp)]; } "
                        + "function addUser(string _stamp, address _addr, bytes1 _userPrivilege) "
                        + "onlyOwner{ userList[sha3(_stamp)] = _addr; userPrivilege[_addr] = _userPrivilege; "
                        + "UserAdded(_stamp); } function addAddress(string _stamp, address _addr) "
                        + "onlyOwner{ userList[sha3(_stamp)] = _addr; AddressAdded(_addr); } }";

        // compile code
        result = api.getTx().compile(source_personnel).getObject();
        CompileResponse contract = result.get("Personnel");

        // unlock owner
        Address owner = Address.wrap("a0c0cc973a306d31320fe72cad62afaa799d076bbd492ca4d5d5d941adfa12a9");
        boolean isUnlocked = api.getWallet().unlockAccount(owner, "password", 100).getObject();
        System.out.format("\nowner account %s%n", isUnlocked ? "unlocked" : "locked");

        // deploy contract
        ContractDeploy.ContractDeployBuilder builder = new ContractDeploy.ContractDeployBuilder()
                .compileResponse(contract).value(BigInteger.ZERO).nrgPrice(NRG_PRICE_MIN)
                .nrgLimit(NRG_LIMIT_CONTRACT_CREATE_MAX).from(owner).data(ByteArrayWrapper.wrap(Bytesable.NULL_BYTE));

        DeployResponse contractResponse = api.getTx().contractDeploy(builder.createContractDeploy()).getObject();

        // print response
        Hash256 txHash = contractResponse.getTxid();
        contractAccount = contractResponse.getAddress();
        System.out.format("%ntransaction hash:%n\t%s%ncontract address: %n\t%s%n", txHash, contractAccount);

        // get & print receipt
        TxReceipt txReceipt = api.getTx().getTxReceipt(txHash).getObject();
        System.out.format("%ntransaction receipt:%n%s%n", txReceipt);

        // 7.b) deploy contract

        isUnlocked = api.getWallet().unlockAccount(owner, "password", 100).getObject();
        System.out.format("%nowner account %s%n", isUnlocked ? "unlocked" : "locked");

        // clear old deploy
        api.getContractController().clear();

        // deploy contract
        ApiMsg msg = api.getContractController()
                .createFromSource(source_personnel, owner, NRG_LIMIT_CONTRACT_CREATE_MAX, NRG_PRICE_MIN);

        if (msg.isError()) {
            System.out.println("deploy contract failed! " + msg.getErrString());
        } else {
            // get contract
            IContract contractRsp = api.getContractController().getContract();

            // print response
            txHash = contractRsp.getDeployTxId();
            contractAccount = contractRsp.getContractAddress();
            System.out.format("%ntransaction hash:%n\t%s%ncontract address: %n\t%s%n", txHash, contractAccount);

            // get & print receipt
            txReceipt = api.getTx().getTxReceipt(txHash).getObject();
            System.out.format("%ntransaction receipt:%n%s%n", txReceipt);
        }

        // 8. execute a contract function

        // input values
        txHash = Hash256.wrap("0xb35c28a10bc996f1cdd81425e6c90d4c841ed6ba6c7f039e76d448a6c869d7bc");
        String addressToAdd = "a0ab123ab123ab123ab123ab123ab123ab123ab123ab123ab123ab123ab123ab";
        String keyToAdd = "key-ab123";

        // get contract object parameters
        txReceipt = api.getTx().getTxReceipt(txHash).getObject();
        contractAccount = txReceipt.getContractAddress();
        Address ownerAddress = txReceipt.getFrom();
        String abiDefinition = ((Map<String, CompileResponse>) api.getTx().compile(source_personnel).getObject())
                .get("Personnel").getAbiDefString();

        // get contract object using ownerAddress & contractAccount & abiDefinition
        IContract ctr = api.getContractController().getContractAt(ownerAddress, contractAccount, abiDefinition);

        // unlock account
        api.getWallet().unlockAccount(ownerAddress, "password");

        // execute function: adding a user address
        ContractResponse rsp = ctr.newFunction("addAddress").setFrom(ownerAddress).setParam(ISString.copyFrom(keyToAdd))
                .setParam(IAddress.copyFrom(addressToAdd)).setTxNrgLimit(NRG_LIMIT_TX_MAX).setTxNrgPrice(NRG_PRICE_MIN)
                .build().execute().getObject();

        System.out.println("ADD response:\n" + rsp);

        // wait for tx to be processed
        Thread.sleep(30000L);
        // get & print receipt
        txReceipt = api.getTx().getTxReceipt(rsp.getTxHash()).getObject();
        System.out.format("ADD transaction receipt:%n%s%n", txReceipt);

        // 9. eth_call

        // call function: getting a user address
        rsp = ctr.newFunction("getUserAddress").setParam(ISString.copyFrom(keyToAdd)).build().call().getObject();

        System.out.println("GET response:\n" + rsp);

        // checking that received value matches input
        String received = Address.wrap((byte[]) rsp.getData().get(0)).toString();
        if (!received.equals(addressToAdd)) {
            System.out.format("The received value:%n%s%ndoes not match the given parameter:%n%s%n",
                    received,
                    addressToAdd);
        } else {
            System.out.format("The received value:%n%s%nmatches the given parameter:%n%s%n", received, addressToAdd);
        }

        // disconnect from api
        api.destroyApi();

        System.exit(0);
    }
}