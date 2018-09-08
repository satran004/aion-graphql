package org.satran.blockchain.graphql.model;

import java.util.List;

public class ContractEventFilterBean {

    private List<String> addresses;
    private long expireTime;
    private String fromBlock;
    private String toBlock;
    private List<String> topics;

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getFromBlock() {
        return fromBlock;
    }

    public void setFromBlock(String fromBlock) {
        this.fromBlock = fromBlock;
    }

    public String getToBlock() {
        return toBlock;
    }

    public void setToBlock(String toBlock) {
        this.toBlock = toBlock;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}
