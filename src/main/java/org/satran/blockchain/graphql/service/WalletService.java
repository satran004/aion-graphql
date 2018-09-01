package org.satran.blockchain.graphql.service;

import java.util.List;

public interface WalletService {

    public List<String> getAccounts();

    public String getDefaultAccount();

    public String getMinerAccount();

    public boolean lockAccount(String pubAddress, String passphrase);

    public boolean unlockAccount(String pubAddress, String passphrase, int duration);
}
