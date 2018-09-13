package org.satran.blockchain.graphql.impl.aion.service;

import org.aion.api.IContract;
import org.aion.api.sol.ISolidityArg;
import org.aion.api.type.ApiMsg;
import org.aion.api.type.ContractResponse;
import org.aion.base.type.Address;
import org.satran.blockchain.graphql.impl.aion.service.dao.AionBlockchainAccessor;
import org.satran.blockchain.graphql.impl.aion.util.ContractTypeConverter;
import org.satran.blockchain.graphql.impl.aion.util.ModelConverter;
import org.satran.blockchain.graphql.model.ContractBean;
import org.satran.blockchain.graphql.model.ContractResponseBean;
import org.satran.blockchain.graphql.model.Param;
import org.satran.blockchain.graphql.model.input.ContractFunction;
import org.satran.blockchain.graphql.service.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.aion.api.ITx.NRG_LIMIT_TX_MAX;
import static org.aion.api.ITx.NRG_PRICE_MIN;
import static org.satran.blockchain.graphql.impl.aion.util.ContractTypeConverter.populateOutputs;
import static org.satran.blockchain.graphql.impl.aion.util.ContractTypeConverter.populateInputParams;

@Repository
public class ContractServiceImpl implements ContractService{

    private static final Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);

    private AionBlockchainAccessor accessor;

    public ContractServiceImpl(AionBlockchainAccessor accessor) {
        this.accessor = accessor;
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

        if(logger.isDebugEnabled())
            logger.debug("Trying to create contract from source");

        return accessor.call(((apiMsg, api) -> {

            try {

                if(params == null || params.size() == 0) {
                    if(value != null)
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
                populateInputParams(contractFunction, contract);

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
                populateInputParams(contractFunction, contract);

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
}
