package org.satran.aion.graphql.service;

import org.aion.api.IAionAPI;
import org.aion.api.type.*;
import org.satran.aion.graphql.exception.ConnectionException;
import org.satran.aion.graphql.pool.AionConnection;
import org.satran.aion.graphql.pool.AionRpcConnectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TxnService {

    private static final Logger logger = LoggerFactory.getLogger(TxnService.class);

    @Autowired
    private AionChainService chainService;

    @Autowired
    private AionRpcConnectionHelper connectionHelper;

    public TxnService() {

    }

    public List<TxDetails> getTransactions(long fromBlock, long limit) {
        List<TxDetails> transactions = new ArrayList<TxDetails>();

        if(logger.isDebugEnabled())
         logger.debug("Getting transaction -----------");

        if(fromBlock == -1) {
            Block latestBlock = chainService.getLatestBlock();

            if(logger.isDebugEnabled())
                logger.debug("Return block " + latestBlock.getNumber());

            if(latestBlock != null)
                fromBlock = latestBlock.getNumber();

        }

        while (transactions.size() < limit) {
            BlockDetails blockDetails = chainService.getBlock(fromBlock);

            if (blockDetails == null)
                break;


            if (blockDetails.getTxDetails().size() > 0) {
                transactions.addAll(blockDetails.getTxDetails());
            }

            fromBlock--;
        }

        return transactions;

    }
}
