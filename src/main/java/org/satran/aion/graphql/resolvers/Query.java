package org.satran.aion.graphql.resolvers;


import java.util.List;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.aion.api.type.BlockDetails;
import org.aion.api.type.Transaction;
import org.aion.base.type.Hash256;
import org.satran.aion.graphql.service.AionChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Query implements GraphQLQueryResolver {

    @Autowired
    private AionChainService aionService;

    public List<BlockDetails> blocks(long first, long offset) {
        return aionService.getBlocks(first, offset);

    }

    public BlockDetails block(long number) {
        return aionService.getBlock(number);
    }

    public Transaction transaction(Hash256 txHash) {
        return aionService.getTransaction(txHash);
    }
}