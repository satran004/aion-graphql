package org.satran.blockchain.graphql.impl.aion.service;

import org.aion.api.IContract;
import org.aion.api.sol.*;
import org.aion.api.sol.impl.Bool;
import org.aion.api.type.ApiMsg;
import org.aion.api.type.ContractResponse;
import org.aion.api.type.MsgRsp;
import org.aion.api.type.TxReceipt;
import org.aion.base.type.Address;
import org.satran.blockchain.graphql.exception.DataConversionException;
import org.satran.blockchain.graphql.impl.aion.service.dao.AionBlockchainAccessor;
import org.satran.blockchain.graphql.impl.aion.util.ModelConverter;
import org.satran.blockchain.graphql.model.*;
import org.satran.blockchain.graphql.model.input.ContractFunction;
import org.satran.blockchain.graphql.service.ContractService;

import static org.aion.api.ITx.NRG_LIMIT_TX_MAX;
import static org.aion.api.ITx.NRG_PRICE_MIN;
import static org.satran.blockchain.graphql.impl.aion.util.TypeUtil.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ContractServiceImpl implements ContractService{

    private static final Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);

    private AionBlockchainAccessor accessor;

    public ContractServiceImpl(AionBlockchainAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public List<ContractBean> createFromSource​(String source, String from, long nrgLimit, long nrgPrice) {
        if(logger.isDebugEnabled())
            logger.debug("Trying to create contract from source");

        return accessor.call(((apiMsg, api) -> {

            try {
                apiMsg.set(api.getContractController().createFromSource(source, Address.wrap(from), nrgLimit, nrgPrice));

                if (apiMsg.isError()) {
                    logger.error("Error creating contract from source code : {} ", apiMsg.getErrString());
                    throw new RuntimeException(apiMsg.getErrString());
                }

                Object result = apiMsg.getObject();

                Map<Address, String> contractAddresses = api.getContractController().getContractMap();

                List<ContractBean> contracts = new ArrayList<>();

                for (Address contractAdd: contractAddresses.keySet()) {
                    IContract contract = api.getContractController().getContract(contractAdd);

                    contracts.add(ModelConverter.convert(contract));
                }

                return contracts;

            } finally {
                api.getContractController().clear();
            }

        }));
    }

    @Override
    public List<ContractBean> createFromSource​(String source, String from, long nrgLimit, long nrgPrice, BigInteger value) {
        if(logger.isDebugEnabled())
            logger.debug("Trying to create contract from source");

        return accessor.call(((apiMsg, api) -> {

            try {
                apiMsg.set(api.getContractController().createFromSource(source, Address.wrap(from), nrgLimit, nrgPrice, value));

                if (apiMsg.isError()) {
                    logger.error("Error creating contract from source code : {} ", apiMsg.getErrString());
                    throw new RuntimeException(apiMsg.getErrString());
                }

                Object result = apiMsg.getObject();

                Map<Address, String> contractAddresses = api.getContractController().getContractMap();

                List<ContractBean> contracts = new ArrayList<>();

                for (Address contractAdd: contractAddresses.keySet()) {
                    IContract contract = api.getContractController().getContract(contractAdd);

                    contracts.add(ModelConverter.convert(contract));
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
        if(logger.isDebugEnabled())
            logger.debug("Execute contract function: {} ", contractFunction);

        //Prepare params

        return accessor.call(((apiMsg, api) -> {

            try {
                IContract contract = api.getContractController().getContractAt(Address.wrap(fromAddress),Address.wrap(contractAddress),
                        abiDefinition);

                if (contract == null) {
                    logger.error("Contract not found or abi mismatch for given address {}  or abi : {}", contractAddress, abiDefinition);
                    throw new RuntimeException("Contract not found or abi mismatch");
                }

                contract.newFunction(contractFunction.name());
                populateParams(contractFunction, contract);

                if(nrgLimit == 0)
                    contract.setTxNrgLimit(NRG_LIMIT_TX_MAX);

                if(nrgPrice == 0)
                    contract.setTxNrgPrice(NRG_PRICE_MIN);

                if(txValue != 0)
                    contract.setTxValue(txValue);

                ApiMsg apiMsg1 = contract.build().execute();

                if(apiMsg1.isError()) {
                    logger.error("Error invoking contract function contract: {}  function {} : Error {}", contractAddress,
                            contractFunction.toString(), apiMsg1.getErrString());
                    throw new RuntimeException("Error calling contract function " + apiMsg1.getErrString());
                }

                ContractResponse contractResponse = apiMsg1.getObject();

                if(logger.isDebugEnabled())
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
                    throw new RuntimeException("Contract not found or abi mismatch");
                }

                contract.newFunction(contractFunction.name());
                populateParams(contractFunction, contract);

                ApiMsg apiMsg1 = contract.build().call();

                if(apiMsg1.isError()) {
                    logger.error("Error invoking contract function contract: {}  function {} : Error {}", contractAddress,
                            contractFunction.toString(), apiMsg1.getErrString());
                    throw new RuntimeException("Error calling contract function " + apiMsg1.getErrString());
                }

                ContractResponse contractResponse = apiMsg1.getObject();

                ContractResponseBean result = ModelConverter.convert(contractResponse);
                populateOutputs(contractFunction, contractResponse, result);

                return result;
            } finally {
                api.getContractController().clear();
            }
        }));
    }

    private void populateParams(ContractFunction contractFunction, IContract contract) {
        //set params
        for(Param param: contractFunction.getParams()) {

            boolean isArray = false;
            if(param.getValues() != null && param.getValues().size() > 0)
                isArray = true;

            if(param.getType() == SolidityType._address) {

                if(isArray)
                    contract.setParam(IAddress.copyFrom(toStringList(param.getValues())));
                else
                    contract.setParam(IAddress.copyFrom(String.valueOf(param.getValue())));

            } else if(param.getType() == SolidityType._bool) {

                if(isArray)
                    contract.setParam(IBool.copyFrom(toBooleanList(param.getValues())));
                else
                    contract.setParam(IBool.copyFrom(toBoolean(param.getValue())));

            } else if(param.getType() == SolidityType._bytes) {

                if(isArray)
                    contract.setParam(IBytes.copyFrom(toBytesList(param.getValues())));
                else
                    contract.setParam(IBytes.copyFrom(toBytes(String.valueOf(param.getValue()))));

            } else if(param.getType() == SolidityType._dbytes) {

                    contract.setParam(IBytes.copyFrom(toBytes(String.valueOf(param.getValue()))));

            } else if(param.getType() == SolidityType._int) {

                if(isArray)
                    contract.setParam(toIInt(param.getValues()));
                else
                    contract.setParam(toIInt(param.getValue()));

            } else if(param.getType() == SolidityType._uint) {

                if(isArray)
                    contract.setParam(toIInt(param.getValues()));
                else
                    contract.setParam(toIInt(param.getValue()));

            } else if(param.getType() == SolidityType._string) {
                    contract.setParam(ISString.copyFrom(String.valueOf(param.getValue())));
            } else
                throw new DataConversionException("Unable to convert input parameters : " + param);
        }
    }

    private void populateOutputs(ContractFunction contractFunction,
                                 ContractResponse contractResponse, ContractResponseBean responseBean) {

        List<Output> outputParams = contractFunction.getOutputs();

        if(outputParams == null) {
            if(logger.isDebugEnabled()) {
                logger.debug("No output param defined. Please define output attribute in graphql query");
            }
            return;
        }

        List<String> resultData = new ArrayList<>();

        for(int i=0; i<contractResponse.getData().size(); i++) {

            Object outputData = contractResponse.getData().get(i);

            if(outputParams.size() <= i)
                return;

            Output outputParam = outputParams.get(i);

            String value = String.valueOf(convertOutput(outputParam, outputData));

            resultData.add(value);
        }

        responseBean.setData(resultData);

    }

    private Object convertOutput(Output outputParam, Object outputData) {
//
//        boolean isArray = outputParam.isArray();

        if(outputData !=  null) {
            if (logger.isDebugEnabled())
                logger.debug("Output type : " + outputData.getClass());
        }

        return Address.wrap((byte [])outputData);
//
//        if(outputParam.getType() == SolidityType._address) {
//
//            return Address.wrap((byte[]) outputData);
//
//        } else if(outputParam.getType() == SolidityType._bool) {
//
//        } else if(outputParam.getType() == SolidityType._bytes) {
//
//        } else if(outputParam.getType() == SolidityType._dbytes) {
//
//        } else if(outputParam.getType() == SolidityType._int) {
//
//        } else if(outputParam.getType() == SolidityType._uint) {
//
//        } else if(outputParam.getType() == SolidityType._string) {
//            return
//        }

    }

}
