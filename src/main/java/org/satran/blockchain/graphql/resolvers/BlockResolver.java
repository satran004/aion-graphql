package org.satran.blockchain.graphql.resolvers;

import org.satran.blockchain.graphql.exception.DataFetchingException;
import org.satran.blockchain.graphql.model.Block;
import org.satran.blockchain.graphql.service.BlockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlockResolver {

    private Logger logger = LoggerFactory.getLogger(BlockResolver.class);

    private BlockService blockService;

    public BlockResolver(BlockService blockService) {
        this.blockService = blockService;
    }

    public List<Block> blocks(long first, long before) {
        try {
            return blockService.getBlocks(first, before);
        } catch (Exception e) {
            logger.error("Error getting blocks", e);
            throw new DataFetchingException(e.getMessage());
        }

    }

    public Block block(long number) {
        try {
            return blockService.getBlock(number);
        } catch(Exception e) {
            logger.error("Error getting block ", e);
            throw new DataFetchingException(e.getMessage());
        }
    }
}
