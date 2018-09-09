package org.satran.blockchain.graphql.model.input;

public class ContractInput {

    private String from;
    private long txNrgLimit;
    private long txNrgPrice;
    private long txValue;

    private ContractFunction function;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getTxNrgLimit() {
        return txNrgLimit;
    }

    public void setTxNrgLimit(long txNrgLimit) {
        this.txNrgLimit = txNrgLimit;
    }

    public long getTxNrgPrice() {
        return txNrgPrice;
    }

    public void setTxNrgPrice(long txNrgPrice) {
        this.txNrgPrice = txNrgPrice;
    }

    public long getTxValue() {
        return txValue;
    }

    public void setTxValue(long txValue) {
        this.txValue = txValue;
    }
}
