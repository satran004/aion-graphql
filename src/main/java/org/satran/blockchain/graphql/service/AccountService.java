package org.satran.blockchain.graphql.service;

import org.satran.blockchain.graphql.entities.AccountKey;
import org.satran.blockchain.graphql.entities.AccountKeyExport;
import org.satran.blockchain.graphql.entities.input.AccountKeyExportInput;

import java.util.List;

public interface AccountService {

    public List<AccountKey> accountCreate(List<String> passphrase, boolean privateKey);

    public AccountKeyExport accountExport(List<AccountKeyExportInput> keys);

    public AccountKeyExport accountBackup(List<AccountKeyExportInput> keys);

    public boolean accountImport(String privateKey, String passphrase);

}
