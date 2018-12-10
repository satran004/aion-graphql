package org.satran.blockchain.graphql.it;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.aion.api.ITx;
import org.aion.base.type.Address;
import org.aion.base.util.ByteUtil;
import org.aion.solidity.Abi;
import org.aion.solidity.Abi.Function;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.satran.blockchain.graphql.impl.aion.util.AbiUtil;
import org.satran.blockchain.graphql.model.CompileResponseBean;
import org.satran.blockchain.graphql.model.ContractBean;
import org.satran.blockchain.graphql.model.ContractResponseBean;
import org.satran.blockchain.graphql.model.MsgRespBean;
import org.satran.blockchain.graphql.model.Output;
import org.satran.blockchain.graphql.model.TxReceiptBean;
import org.satran.blockchain.graphql.model.input.ContractFunction;
import org.satran.blockchain.graphql.model.input.Param;
import org.satran.blockchain.graphql.model.input.TxArgsInput;
import org.satran.blockchain.graphql.service.ContractService;
import org.satran.blockchain.graphql.service.TxnService;
import org.satran.blockchain.graphql.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContractServiceTest extends AbstractTest{

    @LocalServerPort
    private int port;

    @Autowired
    private ContractService contractService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private TxnService txnService;

    private String contract = "contract Greeter {\n"
        + "    /* Define variable greeting of the type string */\n"
        + "    string greeting;\n"
        + "\n"
        + "    /* This runs when the contract is executed */\n"
        + "    function Greeter(string _greeting) public {\n"
        + "        greeting = _greeting;\n"
        + "    }\n"
        + "    \n"
        + "    function updateGreeting(string _greeting) public {\n"
        + "        greeting = _greeting;\n"
        + "    }\n"
        + "\n"
        + "    /* Main function */\n"
        + "    function greet() constant returns (string) {\n"
        + "        return greeting;\n"
        + "    }\n"
        + "}";

    String address = "a0c0cc973a306d31320fe72cad62afaa799d076bbd492ca4d5d5d941adfa12a9";
    long nrgLimit = 4000000L;
    long nrgPrice = 10000000000L;

    @BeforeClass
    public static void runOnceBeforeClass() {
        System.setProperty("rpc.endpoint", "tcp://192.168.0.96:8547");
    }

    public ContractBean deploy() {
        walletService.unlockAccount(address, "test123", 600);

        java.util.List<Param> initParam = new ArrayList<>();
        Param cParam = new Param();
        cParam.setType("string");
        cParam.setValue("Hello From Test");
        initParam.add(cParam);

        List<ContractBean> contractBean = contractService.createFromSource​(contract, address, nrgLimit, nrgPrice,
            BigInteger.ZERO, initParam);

        ContractBean greeterBean = contractBean.stream().filter(b -> b.getContractName().equals("Greeter"))
            .collect(Collectors.toList()).get(0);

        return greeterBean;
    }

    public ContractBean deploy(String contractFile, String contractName, List<Param> initParams) {
        walletService.unlockAccount(address, "test123", 600);

        List<ContractBean> contracts = contractService.createFromSource​(getContractSource(contractFile),
            address, ITx.NRG_LIMIT_CONTRACT_CREATE_MAX, nrgPrice,
            BigInteger.ZERO, initParams);

        ContractBean contractBean = contracts.stream().filter(b -> b.getContractName().equals(contractName))
            .collect(Collectors.toList()).get(0);

        return contractBean;
    }

    @Test
    public void deployContractAndCallTest() {

        ContractBean greeterBean = deploy();

        ContractFunction contractFunction = new ContractFunction();
        contractFunction.setName("greet");

        contractFunction.setParams(Collections.EMPTY_LIST);

        List<Output> outputs = new ArrayList();
        Output output = new Output();
        output.setType("string");

        contractFunction.setOutputs(outputs);

        ContractResponseBean responseBean = contractService.call(address, greeterBean.getContractAddress(), greeterBean.getAbiDefToString(), contractFunction);

        System.out.println("Response ******* " + responseBean.getData().get(0));

        assert responseBean.getData().get(0).equals("Hello From Test");
    }

    @Test
    public void deployContractAndTxCallTest() {

        ContractBean greeterBean = deploy();

        String res = _callGetMethod(greeterBean);

        System.out.println("###### " + new String(ByteUtil.hexStringToBytes(res)));

        assert new String(ByteUtil.hexStringToBytes(res)).trim().equals("Hello From Test");

        System.out.println("** 2nd resp: " + res);

    }

    private String _callGetMethod(ContractBean greeterBean) {
        ContractFunction contractFunction = new ContractFunction();
        contractFunction.setName("greet");

        contractFunction.setParams(Collections.EMPTY_LIST);

        List<Output> outputs = new ArrayList();
        Output output = new Output();
        output.setType("string");

        contractFunction.setOutputs(outputs);

        //Update trasaction.
        TxArgsInput txArgsInput = contractService.getContractCallPayload(address, greeterBean.getContractAddress(),
            greeterBean.getAbiDefToString(),
            contractFunction, nrgLimit, nrgPrice, 0);

        txArgsInput.setNonce(BigInteger.ZERO);
        return txnService.call(txArgsInput);
    }

    @Test
    public void deployContractAndTxExecTest() {

        ContractBean greeterBean = deploy();

        System.out.println(greeterBean.getContractAddress());
        System.out.println(greeterBean.getAbiDefToString());

        ContractFunction contractFunction = new ContractFunction();
        contractFunction.setName("updateGreeting");

        List<Param> params = new ArrayList();
        Param param = new Param();
        param.setValue("New Hello");
        param.setType("string");

        params.add(param);
        contractFunction.setParams(params);

        //Update trasaction.
        TxArgsInput txArgsInput = contractService.getContractCallPayload(address, greeterBean.getContractAddress(),
            greeterBean.getAbiDefToString(),
            contractFunction, 1000000, nrgPrice, 0);

        txArgsInput.setNonce(BigInteger.ZERO);

        System.out.println(txArgsInput);

       // walletService.unlockAccount(address, "test123", 600);

        MsgRespBean msgRespBean = txnService.sendTransaction(txArgsInput, false);

        System.out.println("###### " + msgRespBean.getTxHash());

        String res = _callGetMethod(greeterBean);

        System.out.println("Response.... " + new String(ByteUtil.hexStringToBytes(res)));

        assert new String(ByteUtil.hexStringToBytes(res)).trim().equals("New Hello");

    }

    @Test
    public void deployContractBySource() {

        walletService.unlockAccount(address, "test123", 600);
        List<Object> params = new ArrayList<>();

        params.add(5);
        params.add(address);
        params.add(true);

        TxReceiptBean txReceiptBean = contractService.deployContractBySource(getContractSource("contracts/TypesTest.sol"), "TypesTest", address,
            ITx.NRG_LIMIT_CONTRACT_CREATE_MAX,
            ITx.NRG_PRICE_MIN, params);

        System.out.println("Contract deployed address: " + txReceiptBean.getContractAddress());

        assert  txReceiptBean.getContractAddress() != null;
        assert txReceiptBean.getContractAddress().length() > 10;

    }

    @Test
    public void deployContractByCode() {

        walletService.unlockAccount(address, "test123", 600);
        List<Object> params = new ArrayList<>();

        params.add(5);
        params.add(address);
        params.add(true);

        CompileResponseBean compileResponseBean = txnService.compile(getContractSource("contracts/TypesTest.sol")).get("TypesTest");

        TxReceiptBean txReceiptBean = contractService.deployContractByCode(compileResponseBean.getCode(),
            compileResponseBean.getAbiDefString(), address,
            ITx.NRG_LIMIT_CONTRACT_CREATE_MAX,
            ITx.NRG_PRICE_MIN, params);

        System.out.println("Contract deployed address: " + txReceiptBean.getContractAddress());

        assert  txReceiptBean.getContractAddress() != null;
        assert txReceiptBean.getContractAddress().length() > 10;

    }

    @Test
    public void deployContractByCodeAndCheckIfDataSetProperly() {

        walletService.unlockAccount(address, "test123", 600);
        List<Object> params = new ArrayList<>();

        params.add(5);
        params.add(address);
        params.add(Boolean.TRUE);

        CompileResponseBean compileResponseBean = txnService.compile(getContractSource("contracts/TypesTest.sol")).get("TypesTest");

        TxReceiptBean txReceiptBean = contractService.deployContractByCode(compileResponseBean.getCode(),
            compileResponseBean.getAbiDefString(), address,
            ITx.NRG_LIMIT_CONTRACT_CREATE_MAX,
            ITx.NRG_PRICE_MIN, params);

        System.out.println("Contract deployed address: " + txReceiptBean.getContractAddress());

        assert  txReceiptBean.getContractAddress() != null;
        assert txReceiptBean.getContractAddress().length() > 10;

        //Get data

        //Get the values through get method
        ContractFunction contractFunction = new ContractFunction();
        contractFunction.setName("get");

        contractFunction.setParams(Collections.EMPTY_LIST);

        //Update trasaction.
        TxArgsInput getTxArgsInput = contractService.getContractCallPayload(address, txReceiptBean.getContractAddress(),
            compileResponseBean.getAbiDefString(),
            contractFunction, ITx.NRG_LIMIT_TX_MAX, nrgPrice, 0);

        Abi abi = Abi.fromJSON(compileResponseBean.getAbiDefString());
        Function function = abi.findFunction(f -> AbiUtil.isSameFunction(f, contractFunction));

        List<Output> outputParams = new ArrayList<>();
        outputParams.add(new Output("uint8"));
        outputParams.add(new Output("address"));
        outputParams.add(new Output("bool"));

        List<?> result = contractService.call(getTxArgsInput, function.toJSON().toString(), outputParams);

        assert result.size() == 3;
        assert ((Address)result.get(1)).toString().equals(address);

    }

    public String getContractSource(String contractFile) {
        try {
            Path path = Paths.get(getClass().getClassLoader()
                .getResource(contractFile).toURI());

            Stream<String> lines = Files.lines(path);
            String data = lines.collect(Collectors.joining("\n"));
            lines.close();

            System.out.println(data);
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getPort() {
        return port;
    }


}
