package org.satran.blockchain.graphql.impl.aion.service;

import static org.aion.api.ITx.NRG_LIMIT_TX_MAX;
import static org.aion.api.ITx.NRG_PRICE_MIN;
import static org.satran.blockchain.graphql.impl.aion.util.ContractTypeConverter.populateInputParams;
import static org.satran.blockchain.graphql.impl.aion.util.ContractTypeConverter.populateOutputs;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.aion.api.IContract;
import org.aion.api.sol.ISolidityArg;
import org.aion.api.type.ApiMsg;
import org.aion.api.type.ContractEvent;
import org.aion.api.type.ContractEventFilter;
import org.aion.api.type.ContractResponse;
import org.aion.base.type.Address;
import org.aion.base.util.ByteArrayWrapper;
import org.aion.base.util.ByteUtil;
import org.aion.solidity.Abi;
import org.aion.solidity.Abi.Constructor;
import org.aion.solidity.Abi.Function;
import org.json.JSONObject;
import org.satran.blockchain.graphql.exception.BlockChainAcessException;
import org.satran.blockchain.graphql.exception.DataConversionException;
import org.satran.blockchain.graphql.impl.aion.service.dao.AionBlockchainAccessor;
import org.satran.blockchain.graphql.impl.aion.service.event.rx.ContractEventHolder;
import org.satran.blockchain.graphql.impl.aion.util.AbiUtil;
import org.satran.blockchain.graphql.impl.aion.util.ContractTypeConverter;
import org.satran.blockchain.graphql.impl.aion.util.ModelConverter;
import org.satran.blockchain.graphql.model.CompileResponseBean;
import org.satran.blockchain.graphql.model.ContractBean;
import org.satran.blockchain.graphql.model.ContractDeployPayload;
import org.satran.blockchain.graphql.model.ContractEventBean;
import org.satran.blockchain.graphql.model.ContractEventFilterBean;
import org.satran.blockchain.graphql.model.ContractResponseBean;
import org.satran.blockchain.graphql.model.MsgRespBean;
import org.satran.blockchain.graphql.model.Output;
import org.satran.blockchain.graphql.model.TxReceiptBean;
import org.satran.blockchain.graphql.model.input.ConstructorArgs;
import org.satran.blockchain.graphql.model.input.ContractFunction;
import org.satran.blockchain.graphql.model.input.Param;
import org.satran.blockchain.graphql.model.input.TxArgsInput;
import org.satran.blockchain.graphql.service.ContractService;
import org.satran.blockchain.graphql.service.TxnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class ContractServiceImpl implements ContractService {

    private static final Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);

    private AionBlockchainAccessor accessor;

    private ContractEventHolder contractEventHolder;

    @Autowired
    private TxnService txnService;

    public ContractServiceImpl(AionBlockchainAccessor accessor, ContractEventHolder contractEventHolder) {
        this.accessor = accessor;
        this.contractEventHolder = contractEventHolder;
    }

    @Override
    public List<ContractBean> createFromSource​(String source, String from, long nrgLimit, long nrgPrice) {

        return createFromSource​(source, from, nrgLimit, nrgPrice, null, null);
    }

    @Override
    public List<ContractBean> createFromSource​(String source, String from, long nrgLimit, long nrgPrice, BigInteger value) {
        return createFromSource​(source, from, nrgLimit, nrgPrice, value, null);
    }

    @Override
    public List<ContractBean> createFromSource​(String source, String from, long nrgLimit, long nrgPrice, BigInteger value, List<Param> params) {

        if (logger.isDebugEnabled())
            logger.debug("Trying to create contract from source");

        return accessor.call(((apiMsg, api) -> {

            try {

                if (params == null || params.size() == 0) {
                    if (value != null)
                        apiMsg.set(api.getContractController().createFromSource(source, Address.wrap(from), nrgLimit, nrgPrice, value));
                    else
                        apiMsg.set(api.getContractController().createFromSource(source, Address.wrap(from), nrgLimit, nrgPrice));

                } else {

                    List<ISolidityArg> solParams = ContractTypeConverter.convertParamsToSolValues(params);
                    apiMsg.set(api.getContractController().createFromSource(source, Address.wrap(from), nrgLimit, nrgPrice, value, solParams));
                }

                if (apiMsg.isError()) {
                    logger.error("Error creating contract from source code : {} ", apiMsg.getErrString());
                    throw new RuntimeException(apiMsg.getErrString());
                }


                //Object result = apiMsg.getObject();

                Map<Address, String> contractAddresses = api.getContractController().getContractMap();

                List<ContractBean> contracts = new ArrayList<>();

                for (Address contractAdd : contractAddresses.keySet()) {
                    IContract contract = api.getContractController().getContract(contractAdd);

                    ContractBean contractBean = ModelConverter.convert(contract);
                    contracts.add(contractBean);
                }

                return contracts;

            } finally {
                api.getContractController().clear();
            }

        }));
    }

    @Override
    public List<ContractBean> createMultiContractsFromSource​(String source, String from, long nrgLimit, long nrgPrice, BigInteger value, List<ConstructorArgs> constructorArgsList) {
        if (logger.isDebugEnabled())
            logger.debug("Trying to create contract from source");

        return accessor.call(((apiMsg, api) -> {

            try {
                Map<String, List<ISolidityArg>> argsMap = new HashMap<>();

                if(constructorArgsList != null) {
                    for (ConstructorArgs args : constructorArgsList) {
                        List<ISolidityArg> solParams = ContractTypeConverter.convertParamsToSolValues(args.getParams());
                        argsMap.put(args.getContractName(), solParams);
                    }
                }

                apiMsg.set(api.getContractController().createFromSource(source, Address.wrap(from), nrgLimit, nrgPrice, value, argsMap));

                if (apiMsg.isError()) {
                    logger.error("Error creating contract from source code : {} ", apiMsg.getErrString());
                    throw new RuntimeException(apiMsg.getErrString());
                }

                Map<Address, String> contractAddresses = api.getContractController().getContractMap();

                List<ContractBean> contracts = new ArrayList<>();

                for (Address contractAdd : contractAddresses.keySet()) {
                    IContract contract = api.getContractController().getContract(contractAdd);

                    ContractBean contractBean = ModelConverter.convert(contract);
                    contracts.add(contractBean);
                }

                return contracts;

            } finally {
                api.getContractController().clear();
            }

        }));
    }

    @Override
    public ContractResponseBean execute(String fromAddress, String contractAddress, String abiDefinition,
                                        ContractFunction contractFunction, long nrgLimit, long nrgPrice, long txValue) {
        if (logger.isDebugEnabled())
            logger.debug("Execute contract function: {} ", contractFunction);

        //Prepare params

        return accessor.call(((apiMsg, api) -> {

            try {
                IContract contract = api.getContractController().getContractAt(Address.wrap(fromAddress), Address.wrap(contractAddress),
                        abiDefinition);

                if (contract == null) {
                    logger.error("Contract not found or abi mismatch for given address {}  or abi : {}", contractAddress, abiDefinition);
                    throw new BlockChainAcessException("Contract not found or abi mismatch");
                }

                contract.newFunction(contractFunction.name());
                populateInputParams(contractFunction, contract);

                if (nrgLimit == 0)
                    contract.setTxNrgLimit(NRG_LIMIT_TX_MAX);
                else
                    contract.setTxNrgLimit(nrgLimit);

                if (nrgPrice == 0)
                    contract.setTxNrgPrice(NRG_PRICE_MIN);
                else
                    contract.setTxNrgPrice(nrgPrice);

                if (txValue != 0)
                    contract.setTxValue(txValue);

                ApiMsg apiMsg1 = contract.build().execute();

                if (apiMsg1.isError()) {
                    logger.error("Error invoking contract function contract: {}  function {} : Error {}", contractAddress,
                            contractFunction.toString(), apiMsg1.getErrString());
                    throw new BlockChainAcessException("Error calling contract function " + apiMsg1.getErrString());
                }

                ContractResponse contractResponse = apiMsg1.getObject();

                if (logger.isDebugEnabled())
                    logger.debug("Contract execute response: {}", contractResponse);

//                TxReceipt txReceipt = api.getTx().getTxReceipt(contractResponse.getTxHash()).getObject();
//
//                if(logger.isDebugEnabled())
//                    logger.info("Execute transaction receipt:%n%s%n", txReceipt);

                return ModelConverter.convert(contractResponse);

            } finally {
                api.getContractController().clear();
            }
        }));
    }

    @Override
    public ContractResponseBean call(String fromAddress, String contractAddress, String abiDefinition, ContractFunction contractFunction) {
        if (logger.isDebugEnabled())
            logger.debug("Call contract function: {} ", contractFunction);

        //Prepare params

        return accessor.call(((apiMsg, api) -> {

            try {
                IContract contract = api.getContractController().getContractAt(Address.wrap(fromAddress), Address.wrap(contractAddress),
                        abiDefinition);

                if (contract == null) {
                    logger.error("Contract not found or abi mismatch for given address {}  or abi : {}", contractAddress, abiDefinition);
                    throw new BlockChainAcessException("Contract not found or abi mismatch");
                }

                contract.newFunction(contractFunction.name());
                populateInputParams(contractFunction, contract);

                ApiMsg apiMsg1 = contract.build().call();

                if (apiMsg1.isError()) {
                    logger.error("Error invoking contract function contract: {}  function {} : Error {}", contractAddress,
                            contractFunction.toString(), apiMsg1.getErrString());
                    throw new BlockChainAcessException("Error calling contract function " + apiMsg1.getErrString());
                }

                ContractResponse contractResponse = apiMsg1.getObject();

                ContractResponseBean result = ModelConverter.convert(contractResponse);

//                Abi abi = Abi.fromJSON(abiDefinition);
//
//                Function function = abi.findFunction( f -> AbiUtil.isSameFunction(f, contractFunction));
//
//                if(function != null) {
//                    function.decode(contractResponse.g)
//                } else
//                    throw new BlockChainAcessException("Invalid function or function definition not found in abi");

                populateOutputs(contractFunction, contractResponse, result);

                return result;
            } finally {
                api.getContractController().clear();
            }
        }));
    }

    @Override
    public List<ContractEventBean> getContractEvents(String ownerAddress, String contractAddress, String abiDefinition, List<String> events, ContractEventFilterBean eventFilterBean,
                                                     List<Output> outputTypes) {
        if (logger.isDebugEnabled())
            logger.debug("Registering for contract events: {} ", events);

        //Prepare params
        return accessor.call(((apiMsg, api) -> {

            try {
                IContract contract = api.getContractController().getContractAt(Address.wrap(ownerAddress), Address.wrap(contractAddress),
                        abiDefinition);

                if (contract == null || contract.error()) {
                    logger.error("Contract not found or abi mismatch for given address or some error while creating the contract {}  or abi : {}", contractAddress, abiDefinition);
                    throw new BlockChainAcessException("Contract not found or abi mismatch or error in creation");
                }

                contract.newEvents(events);

                if(eventFilterBean != null) {
                    ContractEventFilter filter = ModelConverter.convert(eventFilterBean);

                    contract.register(filter);
                } else
                    contract.register();

                List<ContractEvent> contractEvents = contract.getEvents();

                if (contractEvents != null && contractEvents.size() != 0) {

                    List<ContractEventBean> beans = contractEvents.stream()
                            .map(cb -> {
                                ContractEventBean ceb = ModelConverter.convert(cb);

                                List<Object> convertedOutputData = ContractTypeConverter.convertSolidityObjectToJavaObject(outputTypes, cb.getResults());
                                ceb.setResults(convertedOutputData);

                                return ceb;
                            })
                            .collect(Collectors.toList());

                    return beans;
                } else {
                    return Collections.EMPTY_LIST;
                }

            } finally {
                api.getContractController().clear();
            }
        }));
    }

    @Override
    public boolean deregisterAllEvents() {

        contractEventHolder.deleteAll();
        return true;
    }

    @Override
    public TxArgsInput getContractCallPayload(String fromAddress, String contractAddress,
        String abiDefinition, ContractFunction contractFunction, long nrgLimit, long nrgPrice,
        long txValue) {
        if (logger.isDebugEnabled())
            logger.debug("Execute contract function: {} ", contractFunction);

        Abi abi = Abi.fromJSON(abiDefinition);

        Function function = abi.findFunction( f -> AbiUtil.isSameFunction(f, contractFunction));

        if(function == null) {
            throw new DataConversionException("Invalid function or function not found in the abi definition");
        }

        List<Object> args = new ArrayList<>();

        for(Param param: contractFunction.getParams()) {
            if(param.getValue() != null)
                args.add(param.getValue());
            else if(param.getValues() != null)
                args.add(param.getValues());
        }

        byte[] encodedBytes = function.encode(args.toArray());

        String encodedData = ByteArrayWrapper.wrap(encodedBytes).toString();

        TxArgsInput txArgsInput = new TxArgsInput();
        txArgsInput.setFrom(fromAddress);
        txArgsInput.setTo(contractAddress);
        txArgsInput.setNrgPrice(nrgPrice);
        txArgsInput.setNrgLimit(nrgLimit);
        txArgsInput.setValue(BigInteger.valueOf(txValue));
        txArgsInput.setData(encodedData.toString());
        txArgsInput.setEncoding("hex");
        txArgsInput.setNonce(BigInteger.ZERO);

        return txArgsInput;
    }

    @Override
    public TxArgsInput getContractDeployPayload(String compileCode, String abiString, String fromAddress,
        long nrgLimit, long nrgPrice, List<Object> args) {

        if (logger.isDebugEnabled())
            logger.debug("Execute getContractDeployPayload function");

        if(StringUtils.isEmpty(abiString))
            throw new BlockChainAcessException("Abi string cannot be null or empty");

        if(StringUtils.isEmpty(compileCode))
            throw new BlockChainAcessException("Compile binary code cannot be null");

        Abi abi = Abi.fromJSON(abiString);

        if(abi == null)
            throw new BlockChainAcessException("Abi string cannot be parsed");

        Constructor constructor = abi.findConstructor();
        byte[] argumentsBytes = null;

        if(args != null && args.size() > 0)
            argumentsBytes = constructor.encodeArguments(args.toArray());

        byte[] deployData = ByteUtil.merge(ByteUtil.hexStringToBytes(compileCode), argumentsBytes);

        TxArgsInput txArgsInput = new TxArgsInput();
        txArgsInput.setFrom(fromAddress);
        txArgsInput.setTo(""); //For contract deployment. to is empty
        txArgsInput.setNrgPrice(nrgPrice);
        txArgsInput.setNrgLimit(nrgLimit);
        txArgsInput.setValue(BigInteger.ZERO);
        txArgsInput.setData(ByteArrayWrapper.wrap(deployData).toString());
        txArgsInput.setEncoding("hex");
        txArgsInput.setNonce(BigInteger.ZERO);

        return txArgsInput;
    }

    @Override
    public ContractDeployPayload getContractDeployPayloadBySource(String source, String contractName, String fromAddress,
        long nrgLimit, long nrgPrice, List<Object> args) {

        Map<String, CompileResponseBean> resMap = txnService.compile(source);

        if(resMap == null)
            throw new BlockChainAcessException("Contract compilation failed");

        if(StringUtils.isEmpty(contractName))
            throw new BlockChainAcessException("Contract Name cannot be null");

        CompileResponseBean compileResponseBean = resMap.get(contractName);

        if(compileResponseBean == null)
            throw new BlockChainAcessException("CompileResponse not found for the contract : " + contractName);

        Abi abi = Abi.fromJSON(compileResponseBean.getAbiDefString());

        TxArgsInput txArgsInput = getContractDeployPayload(compileResponseBean.getCode(), compileResponseBean.getAbiDefString(), fromAddress,
            nrgLimit, nrgPrice, args);

        ContractDeployPayload contractDeployPayload = new ContractDeployPayload(txArgsInput, compileResponseBean);

        return contractDeployPayload;

    }

    @Override
    public TxReceiptBean deployContractBySource(String source, String contractName, String fromAddress,
        long nrgLimit, long nrgPrice, List<Object> args) {

        ContractDeployPayload contractDeployPayload = getContractDeployPayloadBySource(source, contractName, fromAddress, nrgLimit, nrgPrice, args);

        if(contractDeployPayload == null || contractDeployPayload.getTxArgsInput() == null)
            throw new BlockChainAcessException("Contract deployment failed");

        MsgRespBean resp = txnService.sendTransaction(contractDeployPayload.getTxArgsInput(), false);

        String txHash = resp.getTxHash();

        if(StringUtils.isEmpty(txHash))
            throw new BlockChainAcessException("Contract deployment failed. TxnHash is null");

        TxReceiptBean txReceipt = txnService.getTxReceipt(txHash);

        return txReceipt;
    }

    @Override
    public TxReceiptBean deployContractByCode(String code, String abiString, String fromAddress,
        long nrgLimit, long nrgPrice, List<Object> args) {

        TxArgsInput txArgsInput = getContractDeployPayload(code, abiString, fromAddress, nrgLimit, nrgPrice, args);

        if(txArgsInput == null)
            throw new BlockChainAcessException("Contract deployment failed");

        MsgRespBean resp = txnService.sendTransaction(txArgsInput, false);

        String txHash = resp.getTxHash();

        if(StringUtils.isEmpty(txHash))
            throw new BlockChainAcessException("Contract deployment failed. TxnHash is null");

        TxReceiptBean txReceipt = txnService.getTxReceipt(txHash);

        return txReceipt;
    }

    public List<?> call(TxArgsInput txArgsInput, String abiFunction, List<Output> outputTypes) {

        if(txArgsInput == null)
            throw new BlockChainAcessException("TxArgsInput can not be null");

        if(StringUtils.isEmpty(abiFunction)) {
            throw new BlockChainAcessException("abiFunction string can not be null");
        }

        String hexResult = txnService.call(txArgsInput);

        JSONObject jsonObject = new JSONObject(abiFunction);

        Function function = Abi.Function.fromJSON(jsonObject);

        byte[] bytes = ByteUtil.hexStringToBytes(hexResult);

        List retObjs = function.decodeResult(bytes);

        if(outputTypes == null || outputTypes.size() == 0)
            return retObjs;
        else {
            return (List<?>)ContractTypeConverter.convertSolidityObjectToJavaObject(outputTypes, retObjs);
        }

    }
}
