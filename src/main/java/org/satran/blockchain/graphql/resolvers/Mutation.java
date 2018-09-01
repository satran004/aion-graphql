package org.satran.blockchain.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.aion.api.type.CompileResponse;
import org.satran.blockchain.graphql.exception.DataFetchingException;
import org.satran.blockchain.graphql.model.AccountKey;
import org.satran.blockchain.graphql.model.AccountKeyExport;
import org.satran.blockchain.graphql.model.input.AccountKeyExportInput;
import org.satran.blockchain.graphql.service.AccountService;
import org.satran.blockchain.graphql.service.TxnService;
import org.satran.blockchain.graphql.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Mutation implements GraphQLMutationResolver {

    private static final Logger logger = LoggerFactory.getLogger(Mutation.class);

    @Autowired
    private WalletMutator walletMutator;

    @Autowired
    private AccountMutator accountMutator;

    public AccountMutator accountApi() {
        return accountMutator;
    }

    public WalletMutator walletApi() {
        return walletMutator;
    }

}
