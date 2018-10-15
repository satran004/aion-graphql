package org.satran.blockchain.graphql.model;

import java.math.BigInteger;
import java.util.List;
import org.satran.blockchain.graphql.resolvers.AccountResolver;

public class Account {

    private String address;
    private BigInteger balance;
    private List<TxDetails> transactions;

    public Account() {

    }

    public Account(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigInteger getBalance() {

        if (balance == null && address != null && !address.isEmpty()) {
            Account account = AccountResolver.getInstance().account(address, -1);
            if (account != null) {
                balance = account.getBalance();
            }
        }

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
