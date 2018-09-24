package org.satran.blockchain.graphql.model;

import java.math.BigInteger;
import java.util.List;

public class Block {

    private Long number;
    private Long nrgConsumed;
    private Long nrgLimit;
    private String bloom;
    private String extraData;
    private String solution;
    private String hash;
    private String parentHash;
    private BigInteger nonce;
    private BigInteger difficulty;
    private BigInteger totalDifficulty;
    private String minerAddress;
    private String stateRoot;
    private String txTrieRoot;
    private int size;
    private List<TxDetails> txDetails;
    private Long blockTime;
    private Long timestamp;


    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getNrgConsumed() {
        return nrgConsumed;
    }

    public void setNrgConsumed(Long nrgConsumed) {
        this.nrgConsumed = nrgConsumed;
    }

    public Long getNrgLimit() {
        return nrgLimit;
    }

    public void setNrgLimit(Long nrgLimit) {
        this.nrgLimit = nrgLimit;
    }

    public String getBloom() {
        return bloom;
    }

    public void setBloom(String bloom) {
        this.bloom = bloom;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getParentHash() {
        return parentHash;
    }

    public void setParentHash(String parentHash) {
        this.parentHash = parentHash;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public BigInteger getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(BigInteger difficulty) {
        this.difficulty = difficulty;
    }

    public BigInteger getTotalDifficulty() {
        return totalDifficulty;
    }

    public void setTotalDifficulty(BigInteger totalDifficulty) {
        this.totalDifficulty = totalDifficulty;
    }

    public String getMinerAddress() {
        return minerAddress;
    }

    public void setMinerAddress(String minerAddress) {
        this.minerAddress = minerAddress;
    }

    public String getStateRoot() {
        return stateRoot;
    }

    public void setStateRoot(String stateRoot) {
        this.stateRoot = stateRoot;
    }

    public String getTxTrieRoot() {
        return txTrieRoot;
    }

    public void setTxTrieRoot(String txTrieRoot) {
        this.txTrieRoot = txTrieRoot;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int  size) {
        this.size = size;
    }

    public List<TxDetails> getTxDetails() {
        return txDetails;
    }

    public void setTxDetails(List<TxDetails> txDetails) {
        this.txDetails = txDetails;
    }

    public Long getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(Long blockTime) {
        this.blockTime = blockTime;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
