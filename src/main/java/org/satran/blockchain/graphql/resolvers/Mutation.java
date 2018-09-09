package org.satran.blockchain.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mutation implements GraphQLMutationResolver {

    private static final Logger logger = LoggerFactory.getLogger(Mutation.class);

    @Autowired
    private WalletMutator walletMutator;

    @Autowired
    private AccountMutator accountMutator;

    @Autowired
    private TxnMutator txnMutator;

    @Autowired
    private ContractMutator contractMutator;

    public AccountMutator accountApi() {
        return accountMutator;
    }

    public WalletMutator walletApi() {
        return walletMutator;
    }

    public TxnMutator txnApi() {
        return txnMutator;
    }

    public ContractMutator contractApi() {
        return contractMutator;
    }
}
