package org.satran.blockchain.graphql.model.input;

import java.math.BigInteger;

public class TxArgsInput {
    private String data;
    private String from;
    private String to;
    private BigInteger value;
    private BigInteger nonce;
    private long nrgLimit;
    private long nrgPrice;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

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

    public long getNrgLimit() {
        return nrgLimit;
    }

    public void setNrgLimit(long nrgLimit) {
        this.nrgLimit = nrgLimit;
    }

    public long getNrgPrice() {
        return nrgPrice;
    }

    public void setNrgPrice(long nrgPrice) {
        this.nrgPrice = nrgPrice;
    }

    @Override
    public String toString() {
        return "TxArgsInput{" +
                "data='" + data + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", value=" + value +
                ", nonce=" + nonce +
                ", nrgLimit=" + nrgLimit +
                ", nrgPrice=" + nrgPrice +
                '}';
    }
}
