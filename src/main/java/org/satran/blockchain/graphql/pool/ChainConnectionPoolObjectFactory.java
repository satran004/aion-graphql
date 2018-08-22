package org.satran.blockchain.graphql.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ChainConnectionPoolObjectFactory extends BasePooledObjectFactory<ChainConnection> {

    private static final Logger logger = LoggerFactory.getLogger(ChainConnectionPoolObjectFactory.class);

    private ConnectionFactory connectionFactory;

    @Autowired
    public ChainConnectionPoolObjectFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public ChainConnection create() throws Exception {
        return connectionFactory.createConnection();
    }

    public PooledObject<ChainConnection> wrap(ChainConnection chainConnection) {
        return new DefaultPooledObject<ChainConnection>(chainConnection);
    }

    @Override
    public void destroyObject(PooledObject<ChainConnection> p) throws Exception {
        ChainConnection chainConnection = p.getObject();

        if(chainConnection != null) {
            chainConnection.destroy();
        }
    }

    @Override
    public boolean validateObject(PooledObject<ChainConnection> p) {
        if(p.getObject() != null)
            return p.getObject().validate();
        else
            return super.validateObject(p);
    }
}
