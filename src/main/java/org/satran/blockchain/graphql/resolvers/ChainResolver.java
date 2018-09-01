package org.satran.blockchain.graphql.resolvers;

import org.satran.blockchain.graphql.exception.DataFetchingException;
import org.satran.blockchain.graphql.model.Account;
import org.satran.blockchain.graphql.model.Block;
import org.satran.blockchain.graphql.model.TxDetails;
import org.satran.blockchain.graphql.service.ChainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class ChainResolver {

    private Logger logger = LoggerFactory.getLogger(ChainResolver.class);

    private final ChainService chainService;

    public ChainResolver(ChainService chainService) {
        this.chainService = chainService;
    }

    public Long blockNumber() {
        return chainService.blockNumber();
    }

    public BigInteger balance(String address) {
        return chainService.getBalance(address);
    }

    public BigInteger balanceByBlockNumber(String address, long blockNumber) {
        return chainService.getBalance(address, blockNumber);
    }

    public Block blockByHash(String hash) {
        return chainService.getBlockByHash(hash);
    }

    public Block blockByNumber(long number) {
        return chainService.getBlockByNumber(number);
    }

    public int blockTransactionCountByHash(String hash) {
        return chainService.getBlockTrasactionCountByHash(hash);
    }

    public int blockTransactionCountByNumber(long number) {
        return chainService.getBlockTransactionCountByNumber(number);
    }

    public BigInteger nonce(String address) {
        return chainService.getNonce(address);
    }

    public String storageAt(String address, int position) {
        return chainService.getStorageAt(address, position);
    }

    public String storageAtByBlockNumber(String address, int position, long blockNumber) {
        return chainService.getStorageAt(address, position, blockNumber);
    }

    public TxDetails transactionByBlockNumberAndIndex(long blockNumber, int index) {
        return chainService.getTransactionByBlockNumberAndIndex(blockNumber, index);
    }

    public TxDetails transactionByHash(String txnHash) {
        return chainService.getTransactionByHash(txnHash);
    }

    public long transactionCount(String address, long blockNumber) {
        return chainService.getTransactionCount(address, blockNumber);
    }

}
