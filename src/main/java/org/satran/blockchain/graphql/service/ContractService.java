package org.satran.blockchain.graphql.service;

import org.satran.blockchain.graphql.model.*;
import org.satran.blockchain.graphql.model.input.ContractFunction;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface ContractService {

    public List<ContractBean> createFromSource​(String source, String from, long nrgLimit, long nrgPrice);

    public List<ContractBean> createFromSource​(String source, String from, long nrgLimit, long nrgPrice,
                                                BigInteger value);

    public List<ContractBean> createFromSource​(String source, String from, long nrgLimit, long nrgPrice,
                                                BigInteger value, java.util.List<Param> params);

    public ContractResponseBean execute(String fromAddress, String contractAddress, String abiDefinition,
                                        ContractFunction contractFunction, long nrgLimit, long nrgPrice, long txValue);

    public ContractResponseBean call(String fromAddress, String contractAddress, String abiDefinition,
                                     ContractFunction contractFunction);


}
