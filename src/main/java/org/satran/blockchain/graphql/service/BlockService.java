package org.satran.blockchain.graphql.service;

import org.aion.api.type.Block;
import org.aion.api.type.BlockDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BlockService {

    public List<BlockDetails> getBlocks(Long first, long offset);

    public BlockDetails getBlock(long number);

    public Block getLatestBlock();
}
