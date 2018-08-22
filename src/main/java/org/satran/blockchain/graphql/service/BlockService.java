package org.satran.blockchain.graphql.service;

import org.aion.api.type.BlockDetails;
import org.satran.blockchain.graphql.entities.Block;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BlockService {

    public List<Block> getBlocks(Long first, long offset);

    public Block getBlock(long number);

    public Block getLatestBlock();
}
