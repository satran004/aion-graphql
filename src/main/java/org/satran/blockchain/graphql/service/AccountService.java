package org.satran.blockchain.graphql.service;

import org.satran.blockchain.graphql.model.Account;
import org.satran.blockchain.graphql.model.AccountKey;
import org.satran.blockchain.graphql.model.AccountKeyExport;
import org.satran.blockchain.graphql.model.input.AccountKeyExportInput;

import java.math.BigInteger;
import java.util.List;

public interface AccountService {

    public List<AccountKey> accountCreate(List<String> passphrase, boolean privateKey);

    public AccountKeyExport accountExport(List<AccountKeyExportInput> keys);

    public AccountKeyExport accountBackup(List<AccountKeyExportInput> keys);

    public boolean accountImport(String privateKey, String passphrase);

    //Get services
    public Account getAccount(String publicKey, List<String> fields, long blockNumber);

    public BigInteger getBalance(String publicKey, long blockNumber);

}
