package org.satran.blockchain.graphql.model;

import java.util.List;

public class ContractEventBean {

    private String address;
    private String blockHash;
    private String txHash;
    private String data;
    private long blockNumber;
    private int logIndex;
    private String eventName;
    private boolean removed;
    private int txIndex;
    private List<Object> results;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public int getLogIndex() {
        return logIndex;
    }

    public void setLogIndex(int logIndex) {
        this.logIndex = logIndex;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public int getTxIndex() {
        return txIndex;
    }

    public void setTxIndex(int txIndex) {
        this.txIndex = txIndex;
    }

    public List<Object> getResults() {
        return results;
    }

    public void setResults(List<Object> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "ContractEventBean{" +
                "address='" + address + '\'' +
                ", blockHash='" + blockHash + '\'' +
                ", txHash='" + txHash + '\'' +
                ", data='" + data + '\'' +
                ", blockNumber=" + blockNumber +
                ", logIndex=" + logIndex +
                ", eventName='" + eventName + '\'' +
                ", removed=" + removed +
                ", txIndex=" + txIndex +
                ", results=" + results +
                '}';
    }
}
