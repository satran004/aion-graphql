package org.satran.blockchain.graphql.service;

import org.satran.blockchain.graphql.model.ContractBean;
import org.satran.blockchain.graphql.model.ContractResponseBean;
import org.satran.blockchain.graphql.model.SolidityArgBean;
import org.satran.blockchain.graphql.model.TxReceiptBean;
import org.satran.blockchain.graphql.model.input.ContractFunction;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface ContractService {

    public List<ContractBean> createFromSource​(String source, String from, long nrgLimit, long nrgPrice);

    public List<ContractBean> createFromSource​(String source, String from, long nrgLimit, long nrgPrice,
                                                BigInteger value);

    public ContractResponseBean execute(String fromAddress, String contractAddress, String abiDefinition,
                                        ContractFunction contractFunction, long nrgLimit, long nrgPrice, long txValue);

    public ContractResponseBean call(String fromAddress, String contractAddress, String abiDefinition, ContractFunction contractFunction);


//
//    public call();

//
//    public boolean createFromSource​(String source,
//                                     String from,
//                                     long nrgLimit,
//                                     long nrgPrice,
//                                     List<SolidityArgBean> params);
//
//    public boolean createFromSource​(String source,
//                                     String from,
//                                     long nrgLimit,
//                                     long nrgPrice,
//                                     Map<String, List<SolidityArgBean>> params);
//
//    public boolean createFromSource​(String source,
//                                     String from,
//                                     long nrgLimit,
//                                     long nrgPrice,
//                                     BigInteger value,
//                                     List<SolidityArgBean> params);
//
//    public boolean createFromSource​(java.lang.String source,
//                      org.aion.base.type.Address from,
//                      long nrgLimit,
//                      long nrgPrice,
//                      java.math.BigInteger value,
//                      java.util.Map<java.lang.String,java.util.List<SolidityArgBean>> params);
//
//    //Get contract methods
    //    //public ContractBean getContractAt​(String from, String contractAddress, String abi);
//
//








}
