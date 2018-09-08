package org.satran.blockchain.graphql.model;

import java.util.List;

public class TxReceiptBean {

    private String blockHash;
    private long blockNumber;
    private String contractAddress;
    private long cumulativeNrgUsed;
    private String from;
    private long nrgConsumed;
    private String to;
    private String txHash;
    private int txIndex;
    private List<TxLogBean> txLogs;


    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public long getCumulativeNrgUsed() {
        return cumulativeNrgUsed;
    }

    public void setCumulativeNrgUsed(long cumulativeNrgUsed) {
        this.cumulativeNrgUsed = cumulativeNrgUsed;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getNrgConsumed() {
        return nrgConsumed;
    }

    public void setNrgConsumed(long nrgConsumed) {
        this.nrgConsumed = nrgConsumed;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public int getTxIndex() {
        return txIndex;
    }

    public void setTxIndex(int txIndex) {
        this.txIndex = txIndex;
    }

    public List<TxLogBean> getTxLogs() {
        return txLogs;
    }

    public void setTxLogs(List<TxLogBean> txLogs) {
        this.txLogs = txLogs;
    }

    public static class TxLogBean {
        private String address;
        private String data;
        private List<String> topic;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public List<String> getTopic() {
            return topic;
        }

        public void setTopic(List<String> topic) {
            this.topic = topic;
        }
    }
}
