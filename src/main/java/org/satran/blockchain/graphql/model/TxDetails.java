package org.satran.blockchain.graphql.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigInteger;
import org.satran.blockchain.graphql.resolvers.AccountResolver;
import org.springframework.hateoas.ResourceSupport;

public class TxDetails extends ResourceSupport {

    private String from;
    private String to;
    private String txHash;
    private BigInteger value;
    private BigInteger nonce;
    private Long nrgConsumed;
    private Long nrgPrice;
    private String data;
    private Integer txIndex;
    private String contract;
    private Long timestamp;
    private String error;

    private long blockNumber;
    private String blockHash;

    //Ignore only for rest. Graphql should not have any issue
    @JsonIgnore
    private Account fromAccount;
    @JsonIgnore
    private Account toAccount;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public Long getNrgConsumed() {
        return nrgConsumed;
    }

    public void setNrgConsumed(Long nrgConsumed) {
        this.nrgConsumed = nrgConsumed;
    }

    public Long getNrgPrice() {
        return nrgPrice;
    }

    public void setNrgPrice(Long nrgPrice) {
        this.nrgPrice = nrgPrice;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getTxIndex() {
        return txIndex;
    }

    public void setTxIndex(Integer txIndex) {
        this.txIndex = txIndex;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public Account getFromAccount() {
        if(fromAccount == null)
            fromAccount = AccountResolver.getInstance().account(from, -1);

        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        if(toAccount == null)
            toAccount = AccountResolver.getInstance().account(to, -1);

        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }
}
