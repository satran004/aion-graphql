package org.satran.blockchain.graphql.impl.aion.service;

import org.aion.api.IAionAPI;
import org.aion.api.type.ApiMsg;
import org.aion.api.type.Key;
import org.aion.api.type.KeyExport;
import org.aion.base.type.Address;
import org.satran.blockchain.graphql.entities.AccountKey;
import org.satran.blockchain.graphql.entities.AccountKeyExport;
import org.satran.blockchain.graphql.entities.input.AccountKeyExportInput;
import org.satran.blockchain.graphql.exception.ConnectionException;
import org.satran.blockchain.graphql.exception.DataFetchingException;
import org.satran.blockchain.graphql.impl.aion.pool.AionConnection;
import org.satran.blockchain.graphql.pool.ConnectionHelper;
import org.satran.blockchain.graphql.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private ConnectionHelper connectionHelper;

    @Override
    public List<AccountKey> accountCreate(List<String> passphrase, boolean privateKey) {
        if(logger.isDebugEnabled())
            logger.debug("Creating new account");

        if(passphrase == null || passphrase.size() == 0)
            return Collections.EMPTY_LIST;

        AionConnection connection = (AionConnection) connectionHelper.getConnection();

        if(connection == null)
            throw new ConnectionException("Connection could not be established");

        IAionAPI api = connection.getApi();
        ApiMsg apiMsg = connection.getApiMsg();

        try {
            apiMsg.set(api.getAccount().accountCreate(passphrase, privateKey));
            if (apiMsg.isError()) {
                logger.error("Unable to create account" + apiMsg.getErrString());
                throw new RuntimeException(apiMsg.getErrString());
            }

            if(logger.isDebugEnabled())
                logger.debug("Accounts created");

            List<Key> keys = apiMsg.getObject();

            if(keys == null || keys.size() == 0)
                throw new RuntimeException("Account cannot be created");

            return keys.stream()
                    .map(k -> new AccountKey(k.getPassPhrase(), k.getPubKey().toString(), k.getPriKey().toString()))
                    .collect(Collectors.toList());

        } finally {
            connectionHelper.closeConnection(connection);
        }

    }

    @Override
    public AccountKeyExport accountExport(List<AccountKeyExportInput> keys) {
        if(logger.isDebugEnabled())
            logger.debug("Exporting accounts");

        if(keys == null || keys.size() == 0)
            return new AccountKeyExport(Collections.EMPTY_LIST, Collections.EMPTY_LIST);

        AionConnection connection = (AionConnection) connectionHelper.getConnection();

        if(connection == null)
            throw new ConnectionException("Connection could not be established");

        IAionAPI api = connection.getApi();
        ApiMsg apiMsg = connection.getApiMsg();

        List<Key> aionKeys = keys.stream()
                .map(k -> new Key(Address.wrap(k.getPublicKey()), k.getPassphrase()))
                .collect(Collectors.toList());

        try {
            apiMsg.set(api.getAccount().accountExport(aionKeys));
            if (apiMsg.isError()) {
                logger.error("Unable to export accounts" + apiMsg.getErrString());
                throw new RuntimeException(apiMsg.getErrString());
            }

            if(logger.isDebugEnabled())
                logger.debug("Accounts exported");

            KeyExport exportKey = apiMsg.getObject();

            if(exportKey == null)
                throw new RuntimeException("Accounts cannot be exported");

            List<String> keyfiles = exportKey.getKeyFiles().stream()
                    .map(k -> k.toString())
                    .collect(Collectors.toList());

            List<String> invalidAddresses = exportKey.getInvalidAddress().stream()
                    .map(a -> a.toString())
                    .collect(Collectors.toList());

            AccountKeyExport ake = new AccountKeyExport(keyfiles, invalidAddresses);

            return ake;

        } finally {
            connectionHelper.closeConnection(connection);
        }

    }

    @Override
    public AccountKeyExport accountBackup(List<AccountKeyExportInput> keys) {
        if(logger.isDebugEnabled())
            logger.debug("Backingup accounts");

        if(keys == null || keys.size() == 0)
            return new AccountKeyExport(Collections.EMPTY_LIST, Collections.EMPTY_LIST);

        AionConnection connection = (AionConnection) connectionHelper.getConnection();

        if(connection == null)
            throw new ConnectionException("Connection could not be established");

        IAionAPI api = connection.getApi();
        ApiMsg apiMsg = connection.getApiMsg();

        List<Key> aionKeys = keys.stream()
                .map(k -> new Key(Address.wrap(k.getPublicKey()), k.getPassphrase()))
                .collect(Collectors.toList());

        try {
            apiMsg.set(api.getAccount().accountBackup(aionKeys));
            if (apiMsg.isError()) {
                logger.error("Unable to backup accounts" + apiMsg.getErrString());
                throw new RuntimeException(apiMsg.getErrString());
            }

            if(logger.isDebugEnabled())
                logger.debug("Accounts backup");

            KeyExport exportKey = apiMsg.getObject();

            if(exportKey == null)
                throw new RuntimeException("Accounts cannot be backedup");

            List<String> keyfiles = exportKey.getKeyFiles().stream()
                    .map(k -> k.toString())
                    .collect(Collectors.toList());

            List<String> invalidAddresses = exportKey.getInvalidAddress().stream()
                    .map(a -> a.toString())
                    .collect(Collectors.toList());

            AccountKeyExport ake = new AccountKeyExport(keyfiles, invalidAddresses);

            return ake;

        } finally {
            connectionHelper.closeConnection(connection);
        }

    }


    @Override
    public boolean accountImport(String privateKey, String passphrase) {
        if(logger.isDebugEnabled())
            logger.debug("Importing account");

        if(passphrase == null || privateKey == null)
            throw new RuntimeException("Account with null passphrase or private key cannot be imported");


        AionConnection connection = (AionConnection) connectionHelper.getConnection();

        if(connection == null)
            throw new ConnectionException("Connection could not be established");

        IAionAPI api = connection.getApi();
        ApiMsg apiMsg = connection.getApiMsg();

        try {
            Map<String, String> keyMap = new HashMap<>();
            keyMap.put(privateKey, passphrase);

            apiMsg.set(api.getAccount().accountImport(keyMap));
            if (apiMsg.isError()) {
                logger.error("Unable to import account" + apiMsg.getErrString());
                throw new RuntimeException(apiMsg.getErrString());
            }

            if(logger.isDebugEnabled())
                logger.debug("Account cannot be imported");

            List<String> accounts = apiMsg.getObject();

            if(accounts.size() != 0)
                return true;
            else
                return false;


        } finally {
            connectionHelper.closeConnection(connection);
        }

    }
}
