package org.satran.blockchain.graphql.service;

import org.aion.api.type.Transaction;
import org.aion.base.type.Hash256;
import org.satran.blockchain.graphql.entities.TxDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TxnService {

    public List<TxDetails> getTransactions(long fromBlock, long limit);

    public Transaction getTransaction(Hash256 txHash);

}
