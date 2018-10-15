package org.satran.blockchain.graphql.service;

import java.util.List;
import org.satran.blockchain.graphql.model.Account;

public interface WalletService {

    public List<String> getAddresses();

    public List<Account> getAccounts();

    public Account getDefaultAccount();

    public Account getMinerAccount();

    public boolean lockAccount(String pubAddress, String passphrase);

    public boolean unlockAccount(String pubAddress, String passphrase, int duration);
}
