package org.satran.blockchain.graphql.service;

import org.satran.blockchain.graphql.model.TxDetails;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TxnService {

    public List<TxDetails> getTransactions(long fromBlock, long limit);

    public TxDetails getTransaction(String txHash);

  //  public CompileResponse compile(String code);

}
