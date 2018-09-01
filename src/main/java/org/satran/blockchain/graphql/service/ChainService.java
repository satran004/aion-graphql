package org.satran.blockchain.graphql.service;

import org.satran.blockchain.graphql.model.Block;
import org.satran.blockchain.graphql.model.TxDetails;

import java.math.BigInteger;

public interface ChainService {

    public long blockNumber();

    public BigInteger getBalance(String address);

    public BigInteger getBalance(String address, long blockNumber);

    public Block getBlockByHash(String hash);

    public Block getBlockByNumber(long number);

    public int getBlockTrasactionCountByHash(String hash);

    public int getBlockTransactionCountByNumber(long number);

    public BigInteger getNonce(String address);

    public String getStorageAt(String address, int position);

    public String getStorageAt(String address, int position, long blockNumber);

    public TxDetails getTransactionByBlockNumberAndIndex(long blockNumber, int index);

    public TxDetails getTransactionByHash(String txnHash);

    public long getTransactionCount(String address, long blockNumber);

}
