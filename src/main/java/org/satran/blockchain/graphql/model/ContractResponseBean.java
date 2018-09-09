package org.satran.blockchain.graphql.model;

import java.util.List;

public class ContractResponseBean {
    private boolean constant;
    private List<String> data;
    private String txHash;
    private byte status;
    private String msgHash;
    private String error;

    public boolean isConstant() {
        return constant;
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getMsgHash() {
        return msgHash;
    }

    public void setMsgHash(String msgHash) {
        this.msgHash = msgHash;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
