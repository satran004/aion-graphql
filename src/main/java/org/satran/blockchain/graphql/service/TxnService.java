package org.satran.blockchain.graphql.service;

import org.satran.blockchain.graphql.model.CompileResponseBean;
import org.satran.blockchain.graphql.model.MsgRespBean;
import org.satran.blockchain.graphql.model.TxDetails;
import org.satran.blockchain.graphql.model.input.TxArgsInput;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TxnService {

    public List<TxDetails> getTransactions(long fromBlock, long limit);

    public TxDetails getTransaction(String txHash);

    public CompileResponseBean compile(String code);

    public MsgRespBean sendTransaction(TxArgsInput txArgsInput);

}
