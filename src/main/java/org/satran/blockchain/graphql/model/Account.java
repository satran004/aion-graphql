package org.satran.blockchain.graphql.model;

import java.math.BigInteger;
import java.util.List;

public class Account {

    private String publicKey;

    private BigInteger balance;

    private List<TxDetails> transactions;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public BigInteger getBalance() {
        return balance;
    }

    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }

    public List<TxDetails> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TxDetails> transactions) {
        this.transactions = transactions;
    }
}
