package org.satran.blockchain.graphql.model;

import org.springframework.hateoas.ResourceSupport;

public class MsgRespBean extends ResourceSupport {

    private String status;
    private String msgHash;
    private String txHash;
    private String txResult;
    private String txDeploy;
    private String error;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsgHash() {
        return msgHash;
    }

    public void setMsgHash(String msgHash) {
        this.msgHash = msgHash;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getTxResult() {
        return txResult;
    }

    public void setTxResult(String txResult) {
        this.txResult = txResult;
    }

    public String getTxDeploy() {
        return txDeploy;
    }

    public void setTxDeploy(String txDeploy) {
        this.txDeploy = txDeploy;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
