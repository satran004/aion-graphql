package org.satran.blockchain.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Query implements GraphQLQueryResolver {

    private static final Logger logger = LoggerFactory.getLogger(Query.class);

    @Autowired
    private BlockResolver blockResolver;

    @Autowired
    private TxnResolver txnResolver;

    @Autowired
    private AccountResolver accountResolver;

    @Autowired
    private AdminResolver adminResolver;

    @Autowired
    private ChainResolver chainResolver;

    @Autowired
    private NetInfoResolver netInfoResolver;

    @Autowired
    private WalletResolver walletResolver;

    @Autowired
    private ContractQuery contractQuery;


    public BlockResolver blockApi() {
        return blockResolver;
    }

    public TxnResolver txnApi() {
        return txnResolver;
    }

    public AccountResolver accountApi() {
        return accountResolver;
    }

    public AdminResolver adminApi() {
        return adminResolver;
    }

    public ChainResolver chainApi() {
        return chainResolver;
    }

    public NetInfoResolver netApi() {
        return netInfoResolver;
    }

    public WalletResolver walletApi() {
        return walletResolver;
    }

    public ContractQuery contractApi() {
        return contractQuery;
    }

}