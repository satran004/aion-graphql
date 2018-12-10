package org.satran.blockchain.graphql.service;

import org.satran.blockchain.graphql.model.*;
import org.satran.blockchain.graphql.model.input.TxArgsInput;

import java.util.List;
import java.util.Map;

public interface TxnService {

    public String call(TxArgsInput args);

    public byte[] callBytes(TxArgsInput args);

    public Map<String, CompileResponseBean> compile(String code);

    public List<DeployResponseBean> contractDeploy(ContractDeployBean cd);

    public long estimateNrg(String code);

    public long estimateNrg(TxArgsInput argsInput);

    public long estimateNrgFromSource(String source);

    public boolean eventDeregister(List<String> evts, String address);

    public boolean eventRegister(List<String> evts, ContractEventFilterBean ef, String address);

    public boolean fastTxBuild(TxArgsInput args, boolean call);

    public String getCode(String address);

    public String getCode(String address, long blockNumber);

    public MsgRespBean getMsgStatus(String msgHash);

    public long getNrgPrice();

    public String getSolcVersion();

    public TxReceiptBean getTxReceipt(String txnHash);

    public MsgRespBean sendRawTransaction(String encodedTx, boolean async);

    public MsgRespBean sendSignedTransaction(TxArgsInput txArgsInput, String privateKey, boolean async);

    public MsgRespBean sendTransaction(TxArgsInput txArgsInput, boolean async);


    //additional methods
    public List<TxDetails> getTransactions(long before, long first);

    public TxDetails getTransaction(String txHash);

    public List<TxDetails> getTransactionsByHash(List<String> txHash);


}
