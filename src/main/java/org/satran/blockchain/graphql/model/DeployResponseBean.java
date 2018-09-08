package org.satran.blockchain.graphql.model;

public class DeployResponseBean {

    private String address;

    private String txId;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }
}
