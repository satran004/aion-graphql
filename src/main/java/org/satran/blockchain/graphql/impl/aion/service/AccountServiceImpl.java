package org.satran.blockchain.graphql.impl.aion.service;

import org.aion.api.type.Key;
import org.aion.api.type.KeyExport;
import org.aion.base.type.Address;
import org.satran.blockchain.graphql.impl.aion.service.dao.AionBlockchainAccessor;
import org.satran.blockchain.graphql.exception.BlockChainAcessException;
import org.satran.blockchain.graphql.model.Account;
import org.satran.blockchain.graphql.model.AccountKey;
import org.satran.blockchain.graphql.model.AccountKeyExport;
import org.satran.blockchain.graphql.model.input.AccountKeyExportInput;
import org.satran.blockchain.graphql.service.AccountService;
import org.satran.blockchain.graphql.service.ChainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private AionBlockchainAccessor accessor;

    private ChainService chainService;

    @Value("${aion.graphql.disable-sensitive-operations:false}")
    private boolean disableSensitiveOperations;

    private AccountServiceImpl(AionBlockchainAccessor accessor, ChainService chainService) {
        this.accessor = accessor;
        this.chainService = chainService;
    }

    @Override
    public List<AccountKey> accountCreate(List<String> passphrase, boolean privateKey) {
        if (logger.isDebugEnabled())
            logger.debug("Creating new account");

        if(disableSensitiveOperations)
            throw new BlockChainAcessException("This operation is disabled in this deployment due to security reason.");

        if (passphrase == null || passphrase.size() == 0)
            return Collections.EMPTY_LIST;

        return accessor.call(((apiMsg, api) -> {
            apiMsg.set(api.getAccount().accountCreate(passphrase, privateKey));
            if (apiMsg.isError()) {
                logger.error("Unable to create account" + apiMsg.getErrString());
                throw new BlockChainAcessException(apiMsg.getErrString());
            }

            if (logger.isDebugEnabled())
                logger.debug("Accounts created");

            List<Key> keys = apiMsg.getObject();

            if (keys == null || keys.size() == 0)
                throw new BlockChainAcessException("Account cannot be created");

            return keys.stream()
                    .map(k -> new AccountKey(k.getPassPhrase(), k.getPubKey().toString(), k.getPriKey().toString()))
                    .collect(Collectors.toList());

        }));

    }

    @Override
    public AccountKeyExport accountExport(List<AccountKeyExportInput> keys) {
        if (logger.isDebugEnabled())
            logger.debug("Exporting accounts");

        if (keys == null || keys.size() == 0)
            return new AccountKeyExport(Collections.EMPTY_LIST, Collections.EMPTY_LIST);


        List<Key> aionKeys = keys.stream()
                .map(k -> new Key(Address.wrap(k.getAddress()), k.getPassphrase()))
                .collect(Collectors.toList());

        return accessor.call(((apiMsg, api) -> {

            apiMsg.set(api.getAccount().accountExport(aionKeys));
            if (apiMsg.isError()) {
                logger.error("Unable to export accounts" + apiMsg.getErrString());
                throw new BlockChainAcessException(apiMsg.getErrString());
            }

            if (logger.isDebugEnabled())
                logger.debug("Accounts exported");

            KeyExport exportKey = apiMsg.getObject();

            if (exportKey == null)
                throw new BlockChainAcessException("Accounts cannot be exported");

            List<String> keyfiles = exportKey.getKeyFiles().stream()
                    .map(k -> k.toString())
                    .collect(Collectors.toList());

            List<String> invalidAddresses = exportKey.getInvalidAddress().stream()
                    .map(a -> a.toString())
                    .collect(Collectors.toList());

            AccountKeyExport ake = new AccountKeyExport(keyfiles, invalidAddresses);

            return ake;

        }));

    }

    @Override
    public AccountKeyExport accountBackup(List<AccountKeyExportInput> keys) {
        if (logger.isDebugEnabled())
            logger.debug("Backingup accounts");

        if (keys == null || keys.size() == 0)
            return new AccountKeyExport(Collections.EMPTY_LIST, Collections.EMPTY_LIST);


        List<Key> aionKeys = keys.stream()
                .map(k -> new Key(Address.wrap(k.getAddress()), k.getPassphrase()))
                .collect(Collectors.toList());

        return accessor.call(((apiMsg, api) -> {
            apiMsg.set(api.getAccount().accountBackup(aionKeys));
            if (apiMsg.isError()) {
                logger.error("Unable to backup accounts" + apiMsg.getErrString());
                throw new BlockChainAcessException(apiMsg.getErrString());
            }

            if (logger.isDebugEnabled())
                logger.debug("Accounts backup");

            KeyExport exportKey = apiMsg.getObject();

            if (exportKey == null)
                throw new BlockChainAcessException("Accounts cannot be backedup");

            List<String> keyfiles = exportKey.getKeyFiles().stream()
                    .map(k -> k.toString())
                    .collect(Collectors.toList());

            List<String> invalidAddresses = exportKey.getInvalidAddress().stream()
                    .map(a -> a.toString())
                    .collect(Collectors.toList());

            AccountKeyExport ake = new AccountKeyExport(keyfiles, invalidAddresses);

            return ake;

        }));

    }


    @Override
    public boolean accountImport(String privateKey, String passphrase) {
        if (logger.isDebugEnabled())
            logger.debug("Importing account");

        if (passphrase == null || privateKey == null)
            throw new BlockChainAcessException("Account with null passphrase or private key cannot be imported");


        return accessor.call(((apiMsg, api) -> {
            Map<String, String> keyMap = new HashMap<>();
            keyMap.put(privateKey, passphrase);

            apiMsg.set(api.getAccount().accountImport(keyMap));
            if (apiMsg.isError()) {
                logger.error("Unable to import account" + apiMsg.getErrString());
                throw new BlockChainAcessException(apiMsg.getErrString());
            }

            if (logger.isDebugEnabled())
                logger.debug("Account cannot be imported");

            List<String> accounts = apiMsg.getObject();

            if (accounts.size() != 0)
                return true;
            else
                return false;

        }));
    }

    @Override
    public Account getAccount(String address, List<String> fields, long blockNumber) {

        Account account = new Account();
        account.setAddress(address);

        if (fields.contains("balance"))
            account.setBalance(getBalance(address, blockNumber));

        if(fields.contains("nonce")) {
            try {
                account.setNonce(getNonce(address));
            } catch (Exception e) {
                logger.error("Unable to get nonce for address : {}", address);
            }
        }

        return account;

    }


    @Override
    public BigInteger getBalance(String address, long blockNumber) {
        return chainService.getBalance(address, blockNumber);
    }

    @Override
    public BigInteger getNonce(String address) {
        return chainService.getNonce(address);
    }
}
