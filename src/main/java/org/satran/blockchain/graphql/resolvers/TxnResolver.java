package org.satran.blockchain.graphql.resolvers;

import org.satran.blockchain.graphql.exception.DataFetchingException;
import org.satran.blockchain.graphql.model.TxDetails;
import org.satran.blockchain.graphql.service.TxnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TxnResolver {

    private Logger logger = LoggerFactory.getLogger(TxnResolver.class);

    private TxnService txnService;

    public TxnResolver(TxnService txnService) {
        this.txnService = txnService;
    }

    public TxDetails transaction(String txHash) {
        try {
            return txnService.getTransaction(txHash);
        } catch (Exception e) {
            logger.error("Error getting transaction", e);
            throw new DataFetchingException(e.getMessage());
        }
    }

    public List<TxDetails> transactions(long fromBlock, long limit) {
        try {
            return txnService.getTransactions(fromBlock, limit);
        } catch (Exception e) {
            logger.error("Error getting transactions", e);
            throw new DataFetchingException(e.getMessage());
        }
    }
}
