package org.satran.blockchain.graphql.service;

import java.math.BigInteger;
import java.util.List;
import org.satran.blockchain.graphql.model.ContractBean;
import org.satran.blockchain.graphql.model.ContractDeployPayload;
import org.satran.blockchain.graphql.model.ContractEventBean;
import org.satran.blockchain.graphql.model.ContractEventFilterBean;
import org.satran.blockchain.graphql.model.ContractResponseBean;
import org.satran.blockchain.graphql.model.Output;
import org.satran.blockchain.graphql.model.TxReceiptBean;
import org.satran.blockchain.graphql.model.input.ConstructorArgs;
import org.satran.blockchain.graphql.model.input.ContractFunction;
import org.satran.blockchain.graphql.model.input.Param;
import org.satran.blockchain.graphql.model.input.TxArgsInput;

public interface ContractService {

    public List<ContractBean> createFromSource​(String source, String from, long nrgLimit, long nrgPrice);

    public List<ContractBean> createFromSource​(String source, String from, long nrgLimit, long nrgPrice,
                                                BigInteger value);

    public List<ContractBean> createFromSource​(String source, String from, long nrgLimit, long nrgPrice,
                                                BigInteger value, java.util.List<Param> params);

    //Mutiple contracts with constructore args
    public List<ContractBean> createMultiContractsFromSource​(String source, String from, long nrgLimit, long nrgPrice,
                                                BigInteger value, List<ConstructorArgs> constructorArgsList);

    public ContractResponseBean execute(String fromAddress, String contractAddress, String abiDefinition,
                                        ContractFunction contractFunction, long nrgLimit, long nrgPrice, long txValue);

    public ContractResponseBean call(String fromAddress, String contractAddress, String abiDefinition,
                                     ContractFunction contractFunction);

    public List<ContractEventBean> getContractEvents(String ownerAddress, String contractAddress, String abiDefinition,
                                        List<String> events, ContractEventFilterBean eventFilterBean, List<Output> outputTypes);


    public boolean deregisterAllEvents();

    //Methods with data for signing
    public TxArgsInput getContractCallPayload(String fromAddress, String contractAddress, String abiDefinition,
        ContractFunction contractFunction, long nrgLimit, long nrgPrice, long txValue);

    public TxArgsInput getContractDeployPayload(String compileCode, String abiString, String fromAddress,
        long nrgLimit, long nrgPrice, List<Object> args);

    public ContractDeployPayload getContractDeployPayloadBySource(String source, String contractName, String fromAddress,
        long nrgLimit, long nrgPrice, List<Object> args);

    //deploy using default address in kernel
    public TxReceiptBean deployContractBySource(String source, String contractName, String fromAddress,
        long nrgLimit, long nrgPrice, List<Object> args);

    public TxReceiptBean deployContractByCode(String code, String abiString, String fromAddress,
        long nrgLimit, long nrgPrice, List<Object> args);

    public List<?> call(TxArgsInput txArgsInput, String abiFunction, List<Output> outputTypes);

}
