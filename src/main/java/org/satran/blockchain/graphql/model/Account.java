package org.satran.blockchain.graphql.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigInteger;
import java.util.List;
import org.satran.blockchain.graphql.resolvers.AccountResolver;
import org.springframework.hateoas.ResourceSupport;

public class Account extends ResourceSupport {

    private String address;
    private BigInteger balance;
    private BigInteger nonce;

    @JsonIgnore
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

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public List<TxDetails> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TxDetails> transactions) {
        this.transactions = transactions;
    }
}
