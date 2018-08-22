package org.satran.blockchain.graphql.impl.aion.util;

import org.aion.api.type.BlockDetails;
import org.satran.blockchain.graphql.entities.Block;
import org.satran.blockchain.graphql.entities.TxDetails;
import org.springframework.beans.BeanUtils;

import java.util.stream.Collectors;

public class ModelConverter {

    public static Block convert(BlockDetails blockDetails) {
        Block b = new Block();

        BeanUtils.copyProperties(blockDetails, b);

        b.setBloom(blockDetails.getBloom().toString());
        b.setExtraData(blockDetails.getExtraData().toString());
        b.setSolution(blockDetails.getSolution().toString());
        b.setHash(blockDetails.getHash().toString());
        b.setParentHash(blockDetails.getParentHash().toString());
        b.setMinerAddress(blockDetails.getMinerAddress().toString());
        b.setStateRoot(blockDetails.getStateRoot().toString());
        b.setTxTrieRoot(blockDetails.getTxTrieRoot().toString());
        b.setSize(blockDetails.getSize());

        b.setTxDetails(blockDetails.getTxDetails().stream()
                .map(txDetails -> ModelConverter.convert(txDetails))
                .collect(Collectors.toList()));

        return b;
    }

    public static TxDetails convert(org.aion.api.type.TxDetails aionTxDetails) {
        TxDetails tx = new TxDetails();

        BeanUtils.copyProperties(aionTxDetails, tx);

        tx.setTo(aionTxDetails.getTo().toString());
        tx.setFrom(aionTxDetails.getFrom().toString());
        tx.setContract(aionTxDetails.getContract().toString());

        tx.setTxHash(aionTxDetails.getTxHash().toString());
        tx.setData(aionTxDetails.getData().toString());

        return  tx;
    }

    public static Block convert(org.aion.api.type.Block aionBlock) {
        Block block = new Block();

        BeanUtils.copyProperties(aionBlock, block);

        block.setBloom(aionBlock.getBloom().toString());
        block.setExtraData(aionBlock.getExtraData().toString());
        block.setSolution(aionBlock.getSolution().toString());

        block.setHash(aionBlock.getHash().toString());
        block.setParentHash(aionBlock.getParentHash().toString());
        block.setMinerAddress(aionBlock.getMinerAddress().toString());
        block.setStateRoot(aionBlock.getStateRoot().toString());
        block.setTxTrieRoot(aionBlock.getTxTrieRoot().toString());
        block.setSize(aionBlock.getSize());

        return block;
    }
}
