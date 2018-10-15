package org.satran.blockchain.graphql.model;

import java.math.BigInteger;
import java.util.List;

public class Account {

    private String address;

    private BigInteger balance;

    private List<TxDetails> transactions;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
