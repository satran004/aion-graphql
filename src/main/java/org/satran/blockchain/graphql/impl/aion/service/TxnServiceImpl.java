package org.satran.blockchain.graphql.impl.aion.service;

import org.aion.api.IAionAPI;
import org.aion.api.type.ApiMsg;
import org.aion.api.type.BlockDetails;
import org.aion.api.type.Transaction;
import org.aion.base.type.Hash256;
import org.satran.blockchain.graphql.entities.Block;
import org.satran.blockchain.graphql.entities.TxDetails;
import org.satran.blockchain.graphql.exception.ConnectionException;
import org.satran.blockchain.graphql.impl.aion.pool.AionConnection;
import org.satran.blockchain.graphql.pool.ConnectionHelper;
import org.satran.blockchain.graphql.service.TxnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TxnServiceImpl implements TxnService{

    private static final Logger logger = LoggerFactory.getLogger(TxnServiceImpl.class);

    @Autowired
    private BlockServiceImpl chainService;

    @Autowired
    private ConnectionHelper connectionHelper;

    public TxnServiceImpl() {

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
            Block blockDetails = chainService.getBlock(fromBlock);

            if (blockDetails == null)
                break;


            if (blockDetails.getTxDetails().size() > 0) {
                transactions.addAll(blockDetails.getTxDetails());
            }

            fromBlock--;
        }

        return transactions;

    }

    public Transaction getTransaction(Hash256 txHash) {
        if(logger.isDebugEnabled())
            logger.debug("Getting transaction for " + txHash);

        AionConnection connection = (AionConnection) connectionHelper.getConnection();

        if(connection == null)
            throw new ConnectionException("Connection could not be established");

        IAionAPI api = connection.getApi();
        ApiMsg apiMsg = connection.getApiMsg();

        try {
            apiMsg.set(api.getChain().getTransactionByHash(txHash));
            if (apiMsg.isError()) {
                logger.error("Unable to get the transaction" + apiMsg.getErrString());
                throw new RuntimeException(apiMsg.getErrString());
            }

            if(logger.isDebugEnabled())
                logger.debug("Transaction details" + apiMsg.getObject());

            Transaction transaction = apiMsg.getObject();


//            if(blkDetails == null || blkDetails.size() == 0)
//                throw new RuntimeException("No block found with number : " + number);
//
//            BlockDetails block = blkDetails.get(0);
//
//            return block;

            return transaction;

        } finally {
            connectionHelper.closeConnection(connection);
        }
    }
}
