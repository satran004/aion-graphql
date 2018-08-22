package org.satran.blockchain.graphql.impl.aion.service;

import org.aion.api.IAionAPI;
import org.aion.api.type.ApiMsg;
import org.aion.api.type.Block;
import org.aion.api.type.BlockDetails;
import org.satran.blockchain.graphql.exception.ConnectionException;
import org.satran.blockchain.graphql.impl.aion.pool.AionConnection;
import org.satran.blockchain.graphql.pool.ConnectionHelper;
import org.satran.blockchain.graphql.service.BlockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlockServiceImpl implements BlockService {

    private static final Logger logger = LoggerFactory.getLogger(BlockServiceImpl.class);

    @Autowired
    private ConnectionHelper connectionHelper;

    public BlockServiceImpl() {

    }

    public  List<BlockDetails> getBlocks(Long first, long offset) {

        AionConnection connection = (AionConnection) connectionHelper.getConnection();

        if(first > 30)
            throw new RuntimeException("Too many blocks. You can only request upto 30 blocks in a call");

        if(connection == null)
            throw new ConnectionException("Connection could not be established");

        IAionAPI api = connection.getApi();
        ApiMsg apiMsg = connection.getApiMsg();

        //Find the latest block number if the blocknumber is not passed
        if(offset == -1) {
            apiMsg.set(api.getChain().blockNumber());
            if (apiMsg.isError()) {
                logger.error("Get blockNumber isError: " + apiMsg.getErrString());
            }

            long bn = api.getChain().blockNumber().getObject();
        }


        try {

            if(offset == -1)
                apiMsg.set(api.getAdmin().getBlockDetailsByLatest(first));
            else {
                apiMsg.set(api.getAdmin().getBlockDetailsByRange(offset - first, offset));
            }

            if (apiMsg.isError()) {
                logger.error("Error getting block details" + apiMsg.getErrString());
            }

            List<BlockDetails> block = (List<BlockDetails>) apiMsg.getObject();

            if(logger.isDebugEnabled())
                logger.debug("Result: " + block.toString());

            return block;
        } finally {
           connectionHelper.closeConnection(connection);
        }


    }

    public BlockDetails getBlock(long number) {

        if(logger.isDebugEnabled())
            logger.debug("Getting block for " + number);

        AionConnection connection = (AionConnection) connectionHelper.getConnection();

        if(connection == null)
            throw new ConnectionException("Connection could not be established");

        IAionAPI api = connection.getApi();
        ApiMsg apiMsg = connection.getApiMsg();

        try {
            apiMsg.set(api.getAdmin().getBlockDetailsByNumber(String.valueOf(number)));
            if (apiMsg.isError()) {
                logger.error("Unable to get the block" + apiMsg.getErrString());
                throw new RuntimeException(apiMsg.getErrString());
            }

            if(logger.isDebugEnabled())
                logger.debug("Block details got" + apiMsg.getObject().getClass());

            List<BlockDetails> blkDetails = ((List<BlockDetails>)apiMsg.getObject());

            if(blkDetails == null || blkDetails.size() == 0)
                throw new RuntimeException("No block found with number : " + number);

            BlockDetails block = blkDetails.get(0);

            return block;

        } finally {
            connectionHelper.closeConnection(connection);
        }

    }

    public Block getLatestBlock() {
        AionConnection connection = (AionConnection) connectionHelper.getConnection();

        if(connection == null)
            throw new ConnectionException("Connection could not be established");

        IAionAPI api = connection.getApi();
        ApiMsg apiMsg = connection.getApiMsg();

        try {
            if(logger.isDebugEnabled())
                logger.debug("Getting latest block");

            apiMsg.set(api.getAdmin().getBlocksByLatest(1L));

            if (apiMsg.isError()) {
                logger.error("Unable to get the latest block" + apiMsg.getErrString());
                throw new RuntimeException(apiMsg.getErrString());
            }

            Block block = ((List<Block>)apiMsg.getObject()).get(0);

            return block;

        } finally {
            connectionHelper.closeConnection(connection);
        }
    }

}
