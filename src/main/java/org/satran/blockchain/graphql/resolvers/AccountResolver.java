package org.satran.blockchain.graphql.resolvers;

import org.satran.blockchain.graphql.exception.DataFetchingException;
import org.satran.blockchain.graphql.model.Account;
import org.satran.blockchain.graphql.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountResolver {

    private Logger logger = LoggerFactory.getLogger(AccountResolver.class);

    private AccountService accountService;

    public AccountResolver(AccountService accountService) {
        this.accountService = accountService;
    }

    public Account account(String publicKey, long blockNumber) {
        try {
            List<String> fields = new ArrayList<>();
            fields.add("balance");

            return accountService.getAccount(publicKey, fields, blockNumber);
        } catch (Exception e) {
            logger.error("Error getting transaction", e);
            throw new DataFetchingException(e.getMessage());
        }
    }
}
