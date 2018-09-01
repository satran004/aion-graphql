package org.satran.blockchain.graphql.impl.aion.service;

import org.aion.api.type.CompileResponse;
import org.aion.api.type.Transaction;
import org.aion.base.type.Hash256;
import org.satran.blockchain.graphql.impl.aion.service.dao.AionBlockchainAccessor;
import org.satran.blockchain.graphql.impl.aion.util.ModelConverter;
import org.satran.blockchain.graphql.model.Block;
import org.satran.blockchain.graphql.model.TxDetails;
import org.satran.blockchain.graphql.service.BlockService;
import org.satran.blockchain.graphql.service.TxnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TxnServiceImpl implements TxnService {

    private static final Logger logger = LoggerFactory.getLogger(TxnServiceImpl.class);

    private BlockService chainService;

    private AionBlockchainAccessor accessor;

    public TxnServiceImpl(BlockService blockService, AionBlockchainAccessor accessor) {
        this.chainService = blockService;
        this.accessor = accessor;
    }

    public List<TxDetails> getTransactions(long fromBlock, long limit) {
        List<TxDetails> transactions = new ArrayList<TxDetails>();

        if (logger.isDebugEnabled())
            logger.debug("Getting transaction -----------");

        if (fromBlock == -1) {
            Block latestBlock = chainService.getLatestBlock();

            if (logger.isDebugEnabled())
                logger.debug("Return block " + latestBlock.getNumber());

            if (latestBlock != null)
                fromBlock = latestBlock.getNumber();

        }

        while (transactions.size() < limit && fromBlock >= 0) {
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

    public TxDetails getTransaction(String txHash) {
        if (logger.isDebugEnabled())
            logger.debug("Getting transaction for {} ", txHash);

        return accessor.call(((apiMsg, api) -> {
            apiMsg.set(api.getChain().getTransactionByHash(Hash256.wrap(txHash)));
            if (apiMsg.isError()) {
                logger.error("Unable to get the transaction" + apiMsg.getErrString());
                throw new RuntimeException(apiMsg.getErrString());
            }

            if (logger.isDebugEnabled())
                logger.debug("Transaction details" + apiMsg.getObject());

            Transaction transaction = apiMsg.getObject();

            return ModelConverter.convert(transaction);
//            if(blkDetails == null || blkDetails.size() == 0)
//                throw new RuntimeException("No block found with number : " + number);
//
//            BlockDetails block = blkDetails.get(0);
//
//            return block;


        }));

    }

    public CompileResponse compile(String code) {
        if(logger.isDebugEnabled())
            logger.debug("Trying to compile code");

        return accessor.call(((apiMsg, api) -> {
            apiMsg.set(api.getTx().compile(code));

            if(apiMsg.isError()) {
                logger.error("Error compiling contract source code");
                throw new RuntimeException(apiMsg.getErrString());
            }

            return apiMsg.getObject();
        }));

    }
}
