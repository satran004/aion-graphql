package org.satran.blockchain.graphql.pool;

public interface ChainConnection {

    public void destroy();

    public boolean validate();
}
