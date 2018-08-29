package org.satran.blockchain.graphql.impl.aion.util;

import org.aion.api.type.BlockDetails;
import org.aion.api.type.Protocol;
import org.satran.blockchain.graphql.model.Block;
import org.satran.blockchain.graphql.model.ProtocolInfo;
import org.satran.blockchain.graphql.model.TxDetails;
import org.springframework.beans.BeanUtils;

import java.util.stream.Collectors;

public class ModelConverter {

    public static Block convert(BlockDetails blockDetails) {
        Block b = new Block();

        BeanUtils.copyProperties(blockDetails, b);

        b.setBloom(String.valueOf(blockDetails.getBloom()));
        b.setExtraData(String.valueOf(blockDetails.getExtraData()));
        b.setSolution(String.valueOf(blockDetails.getSolution()));
        b.setHash(String.valueOf(blockDetails.getHash()));
        b.setParentHash(String.valueOf(blockDetails.getParentHash()));
        b.setMinerAddress(String.valueOf(blockDetails.getMinerAddress()));
        b.setStateRoot(String.valueOf(blockDetails.getStateRoot()));
        b.setTxTrieRoot(String.valueOf(blockDetails.getTxTrieRoot()));
        b.setSize(blockDetails.getSize());

        b.setTxDetails(blockDetails.getTxDetails().stream()
                .map(txDetails -> ModelConverter.convert(txDetails))
                .collect(Collectors.toList()));

        return b;
    }

    public static TxDetails convert(org.aion.api.type.TxDetails aionTxDetails) {
        TxDetails tx = new TxDetails();

        BeanUtils.copyProperties(aionTxDetails, tx);

        tx.setTo(String.valueOf(aionTxDetails.getTo()));
        tx.setFrom(String.valueOf(aionTxDetails.getFrom()));
        tx.setContract(String.valueOf(aionTxDetails.getContract()));

        tx.setTxHash(String.valueOf(aionTxDetails.getTxHash()));
        tx.setData(String.valueOf(aionTxDetails.getData()));

        return  tx;
    }

    public static Block convert(org.aion.api.type.Block aionBlock) {
        Block block = new Block();

        BeanUtils.copyProperties(aionBlock, block);

        block.setBloom(String.valueOf(aionBlock.getBloom()));
        block.setExtraData(String.valueOf(aionBlock.getExtraData()));
        block.setSolution(String.valueOf(aionBlock.getSolution()));

        block.setHash(String.valueOf(aionBlock.getHash()));
        block.setParentHash(String.valueOf(aionBlock.getParentHash()));
        block.setMinerAddress(String.valueOf(aionBlock.getMinerAddress()));
        block.setStateRoot(String.valueOf(aionBlock.getStateRoot()));
        block.setTxTrieRoot(String.valueOf(aionBlock.getTxTrieRoot()));
        block.setSize(aionBlock.getSize());

        return block;
    }

    public static ProtocolInfo convert(Protocol aionProtocol) {
        ProtocolInfo protocolInfo = new ProtocolInfo();

        BeanUtils.copyProperties(aionProtocol, protocolInfo);

        return protocolInfo;
    }
}
