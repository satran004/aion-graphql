package org.satran.blockchain.graphql;

import org.aion.api.IAionAPI;
import org.aion.api.IContract;
import org.aion.api.sol.ISString;
import org.aion.api.sol.impl.Uint;
import org.aion.api.type.*;
import org.aion.base.type.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.aion.api.ITx.NRG_LIMIT_TX_MAX;
import static org.aion.api.ITx.NRG_PRICE_MIN;

public class ContractTest {

    private static Logger logger = LoggerFactory.getLogger(ContractTest.class);

    private static String url = "tcp://localhost:8548";

    public static void testCompile(IAionAPI api, ApiMsg apiMsg) {

            final String code1 = "pragma solidity ^0.4.22;\n" +
                    "\n" +
                    "contract helloWorld {\n" +
                    " function renderHelloWorld () public pure returns (string) {\n" +
                    "   return 'helloWorld';\n" +
                    " }\n" +
                    "}";

            apiMsg.set(api.getTx().getNrgPrice());

            if (apiMsg.isError()) {
                logger.error("Error compiling contract source code : {} " + apiMsg.getErrString());
                throw new RuntimeException(apiMsg.getErrString());
            }

            logger.info("NRG price: " + apiMsg.getObject());

            apiMsg.set(api.getTx().compile(code1));

            if (apiMsg.isError()) {
                logger.error("Error compiling contract source code : {} " + apiMsg.getErrString());
            }

            logger.info("Compile Respone: " + apiMsg.getObject());

    }

    public static void contractTest(IAionAPI api, ApiMsg apiMsg) {

        // set up the arguments for unlock account operation
        Address account = Address.wrap("a0c0cc973a306d31320fe72cad62afaa799d076bbd492ca4d5d5d941adfa12a9");
        String password = "test123";
        int unlockTimeout = 300;

// unlock an account
        apiMsg.set(api.getWallet().unlockAccount(account, password, unlockTimeout));
        if (apiMsg.isError() || !(boolean) apiMsg.getObject()) {
            System.out.println("Unlock account failed! Please check your password  " + apiMsg.getErrString());
            // cleanup() on return
        }

// get the Smart Contract code from file
        String sc = null;
        try {
//            sc = new String(Files.readAllBytes(Paths.get("/Users/satya/work/crypto-dev/aion-dev/contracts/Greeter.sol")));
            sc = new String(Files.readAllBytes(Paths.get("/Users/satya/work/crypto-dev/aion-dev/contracts/Instructor.sol")));

        } catch (IOException e) {
            e.printStackTrace();
            // safely close API connection
        }

//         sc = "pragma solidity ^0.4.22;\n" +
//                "\n" +
//                "contract helloWorld {\n" +
//                " function renderHelloWorld () public pure returns (string) {\n" +
//                "   return 'helloWorld';\n" +
//                " }\n" +
//                "}";
// contract creation:
// this function blocks until the contract creation transaction
// has been included in at least one block on the Blockchain
        //long energyLimit = 1_000_000L;
        //long energyPrice = 1L;
        // .nrgLimit(1_000_000L)
        //.nrgPrice(10000000000L)
        apiMsg.set(api.getContractController().createFromSource(sc, account, 1_000_000L, 10000000000L));
        if (apiMsg.isError()) {
            System.out.println("Deploy contract failed with error: " + apiMsg.getErrString());
            // safely close API connection
        }

        IContract contract = api.getContractController().getContract();

// make sure to save the ABI definition for the contract since an API
// limitation is that in order to create a Contract object, one needs the
// contract address and the ABI definition
        String contractAddress = contract.getContractAddress().toString();
        String contractAbi = contract.getAbiDefToString();

        System.out.println("Contract addres: " + contractAddress);
        System.out.println("Contract Abi: " + contractAbi);
        System.out.println("Encoded Str: " + contract);
    }

    public static void compile(IAionAPI api, ApiMsg apiMsg) {

        // get NRG price
        long price = api.getTx().getNrgPrice().getObject();

// print price
        System.out.println("current NRG price = " + price + " nAmp");

        String source_ticker = "contract ticker { uint public val; function tick () { val+= 1; } }";

// compile code
        Map<String, CompileResponse> result = api.getTx().compile(source_ticker).getObject();

// print result
        System.out.println(result);
    }

    public static void sendTransaction(IAionAPI api, ApiMsg apiMsg) {
        Address account = Address.wrap("a0c0cc973a306d31320fe72cad62afaa799d076bbd492ca4d5d5d941adfa12a9");
        String password = "test123";
        int unlockTimeout = 300;

        apiMsg.set(api.getWallet().unlockAccount(account, password, unlockTimeout));
        if (apiMsg.isError() || !(boolean) apiMsg.getObject()) {
            System.out.println("Unlock account failed! Please check your password  " + apiMsg.getErrString());
            // cleanup() on return
        }

        TxArgs txArgs = new TxArgs.TxArgsBuilder()
                .from(Address.wrap("a0c0cc973a306d31320fe72cad62afaa799d076bbd492ca4d5d5d941adfa12a9"))
                .to(Address.wrap("a0ee03368d08281ab8d4a0bc70768b25e9363b80b060291b0e045cf763c0d817"))
                .value(BigInteger.valueOf(7000000000000000000L))
               // .nonce(BigInteger.valueOf(1L))
               // .nrgLimit(1_000_000L)
                //.nrgPrice(10000000000L)
                .createTxArgs();


        long energyLimit = 1_000_000L;
        long energyPrice = 1L;


        IContract contract = api.getContractController().getContract();

        apiMsg.set(api.getTx().nonBlock().sendTransaction(txArgs));
        if (apiMsg.isError()) {
            System.out.println("Deploy contract failed with error: " + apiMsg.getErrString());
            // safely close API connection
        }

        MsgRsp obj = apiMsg.getObject();

        logger.info("Returned obj ---- {} : {} : {}" + obj.getError(), obj.getStatus(), obj.getTxHash());

    }

    public static void invokeMethod(IAionAPI api, ApiMsg apiMsg) throws InterruptedException {

        Address ownerAddress = Address.wrap("a0c0cc973a306d31320fe72cad62afaa799d076bbd492ca4d5d5d941adfa12a9");
        Address contractAddress = Address.wrap("a0325401995789edf2287def289d3c6d93c9e0771b47209104cfa1b722f0c9cc");

//      String abiDefinition = "[{\"outputs\":[],\"constant\":false,\"payable\":false,\"inputs\":[{\"name\":\"_stamp\",\"type\":\"string\"},{\"name\":\"_addr\",\"type\":\"address\"},{\"name\":\"_userPrivilege\",\"type\":\"bytes1\"}],\"name\":\"addUser\",\"type\":\"function\"},{\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"constant\":true,\"payable\":false,\"inputs\":[{\"name\":\"_stamp\",\"type\":\"string\"}],\"name\":\"getUserAddress\",\"type\":\"function\"},{\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"constant\":true,\"payable\":false,\"inputs\":[],\"name\":\"owner\",\"type\":\"function\"},{\"outputs\":[],\"constant\":false,\"payable\":false,\"inputs\":[{\"name\":\"_stamp\",\"type\":\"string\"},{\"name\":\"_addr\",\"type\":\"address\"}],\"name\":\"addAddress\",\"type\":\"function\"},{\"outputs\":[],\"payable\":false,\"inputs\":[],\"name\":\"\",\"type\":\"constructor\"},{\"outputs\":[],\"inputs\":[{\"indexed\":false,\"name\":\"_stamp\",\"type\":\"string\"}],\"name\":\"UserAdded\",\"anonymous\":false,\"type\":\"event\"},{\"outputs\":[],\"inputs\":[{\"indexed\":true,\"name\":\"_addr\",\"type\":\"address\"}],\"name\":\"AddressAdded\",\"anonymous\":false,\"type\":\"event\"}]";

        String abiDefinition = "[{\"outputs\":[{\"name\":\"\",\"type\":\"string\"}," +
                "{\"name\":\"\",\"type\":\"uint128\"}],\"constant\":false,\"payable\":false,\"inputs\":[],\"name\"" +
                ":\"getInstructor\",\"type\":\"function\"},{\"outputs\":[],\"constant\":false,\"payable\":false,\"inputs\"" +
                ":[{\"name\":\"_fName\",\"type\":\"string\"},{\"name\":\"_age\",\"type\":\"uint128\"}],\"name\"" +
                ":\"setInstructor\",\"type\":\"function\"},{\"outputs\":[],\"inputs\":[{\"indexed\":false,\"name\":" +
                "\"name\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"age\",\"type\":\"uint128\"}],\"name\":" +
                "\"Instructor\",\"anonymous\":false,\"type\":\"event\"}]";

        IContract ctr = api.getContractController().getContractAt(ownerAddress, contractAddress, abiDefinition);

        // unlock account
        api.getWallet().unlockAccount(ownerAddress, "test123");

//        String addressToAdd = "a0ab123ab123ab123ab123ab123ab123ab123ab123ab123ab123ab123ab123ab";
//        String keyToAdd = "key-ab123";
//
//        System.out.println("CTR:::: " + ctr);
        // execute function: adding a user address


        for(int i=0; i<10;i++) {
            ContractResponse rsp = ctr.newFunction("setInstructor").setFrom(ownerAddress).setParam(ISString.copyFrom("satya"))
                    .setParam(Uint.copyFrom(34)).setTxNrgLimit(NRG_LIMIT_TX_MAX).setTxNrgPrice(NRG_PRICE_MIN)
                    .build().execute().getObject();

            System.out.println("ADD response:\n" + rsp);

            // wait for tx to be processed
            //Thread.sleep(30000L);
            // get & print receipt
           // TxReceipt txReceipt = api.getTx().getTxReceipt(rsp.getTxHash()).getObject();
            System.out.format("ADD transaction receipt:%n%s%n", rsp.getTxHash());
        }
        // 9. eth_call

        // call function: getting a user address
       // rsp = ctr.newFunction("getInstructor").build().setTxNrgLimit(NRG_LIMIT_TX_MAX).setTxNrgPrice(NRG_PRICE_MIN).call().getObject();

    //    System.out.println("GET response:\n" + rsp);

        // checking that received value matches input
      //  String received = Address.wrap((byte[]) rsp.getData().get(0)).toString();
//      if (!received.equals(addressToAdd)) {
//          System.out.format("The received value:%n%s%ndoes not match the given parameter:%n%s%n",
//                    received,
//                    addressToAdd);
//      } else {
//          System.out.format("The received value:%n%s%nmatches the given parameter:%n%s%n", received, addressToAdd);
//      }
    }

    public static void getMethod(IAionAPI api, ApiMsg apiMsg) throws InterruptedException {

        Address ownerAddress = Address.wrap("a0c0cc973a306d31320fe72cad62afaa799d076bbd492ca4d5d5d941adfa12a9");
        Address contractAddress = Address.wrap("a0325401995789edf2287def289d3c6d93c9e0771b47209104cfa1b722f0c9cc");

        // String abiDefinition = "[{\"outputs\":[],\"constant\":false,\"payable\":false,\"inputs\":[{\"name\":\"_stamp\",\"type\":\"string\"},{\"name\":\"_addr\",\"type\":\"address\"},{\"name\":\"_userPrivilege\",\"type\":\"bytes1\"}],\"name\":\"addUser\",\"type\":\"function\"},{\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"constant\":true,\"payable\":false,\"inputs\":[{\"name\":\"_stamp\",\"type\":\"string\"}],\"name\":\"getUserAddress\",\"type\":\"function\"},{\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"constant\":true,\"payable\":false,\"inputs\":[],\"name\":\"owner\",\"type\":\"function\"},{\"outputs\":[],\"constant\":false,\"payable\":false,\"inputs\":[{\"name\":\"_stamp\",\"type\":\"string\"},{\"name\":\"_addr\",\"type\":\"address\"}],\"name\":\"addAddress\",\"type\":\"function\"},{\"outputs\":[],\"payable\":false,\"inputs\":[],\"name\":\"\",\"type\":\"constructor\"},{\"outputs\":[],\"inputs\":[{\"indexed\":false,\"name\":\"_stamp\",\"type\":\"string\"}],\"name\":\"UserAdded\",\"anonymous\":false,\"type\":\"event\"},{\"outputs\":[],\"inputs\":[{\"indexed\":true,\"name\":\"_addr\",\"type\":\"address\"}],\"name\":\"AddressAdded\",\"anonymous\":false,\"type\":\"event\"}]";

        String abiDefinition = "[{\"outputs\":[{\"name\":\"\",\"type\":\"string\"}," +
                "{\"name\":\"\",\"type\":\"uint128\"}],\"constant\":false,\"payable\":false,\"inputs\":[],\"name\"" +
                ":\"getInstructor\",\"type\":\"function\"},{\"outputs\":[],\"constant\":false,\"payable\":false,\"inputs\"" +
                ":[{\"name\":\"_fName\",\"type\":\"string\"},{\"name\":\"_age\",\"type\":\"uint128\"}],\"name\"" +
                ":\"setInstructor\",\"type\":\"function\"},{\"outputs\":[],\"inputs\":[{\"indexed\":false,\"name\":" +
                "\"name\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"age\",\"type\":\"uint128\"}],\"name\":" +
                "\"Instructor\",\"anonymous\":false,\"type\":\"event\"}]";

        IContract ctr = api.getContractController().getContractAt(ownerAddress, contractAddress, abiDefinition);

        // unlock account
        api.getWallet().unlockAccount(ownerAddress, "test123");

        ContractResponse rsp = ctr.newFunction("getInstructor")
                .setTxNrgLimit(NRG_LIMIT_TX_MAX).setTxNrgPrice(NRG_PRICE_MIN).build().call().getObject();

        System.out.println("GET response:\n" + String.valueOf(rsp.getData().get(0)));
        System.out.println("GET response:\n" + String.valueOf(rsp.getData().get(1)));

//      checking that received value matches input
//        String received = Address.wrap((byte[]) rsp.getData().get(0)).toString();
//        if (!received.equals(addressToAdd)) {
//            System.out.format("The received value:%n%s%ndoes not match the given parameter:%n%s%n",
//                    received,
//                    addressToAdd);
//        } else {
//            System.out.format("The received value:%n%s%nmatches the given parameter:%n%s%n", received, addressToAdd);
//        }
    }

    public static void getNodeInfo(IAionAPI api, ApiMsg apiMsg) {

        apiMsg.set(api.getNet().getActiveNodes());

        List<Node> nodeList = apiMsg.getObject();

        nodeList.stream()
                .forEach(node -> {
                    System.out.println(node.getLatency() + "  "  +node.getP2pPort() + "  " + node.getP2pIP());
                });

        System.out.println(nodeList);
    }

    public static void main(String[] args) throws Exception {
        IAionAPI api = IAionAPI.init();

        logger.info("Trying to connect to " + url);
        ApiMsg apiMsg = api.connect(url);

        if (apiMsg.isError()) {
            logger.error("Connect server failed, exit test! " + apiMsg.getErrString());
        }

        try {

//            contractTest(api, apiMsg);
            //getNodeInfo(api, apiMsg);
            invokeMethod(api, apiMsg);
//            getMethod(api, apiMsg);
         //   contractTest(api, apiMsg);
        } finally {
            api.destroyApi();
        }

    }
}
