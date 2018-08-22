package org.satran.blockchain.graphql.pool;

import org.springframework.stereotype.Service;

@Service
public interface ConnectionFactory {

    public ChainConnection createConnection();
}
