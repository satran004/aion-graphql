package org.satran.aion.graphql.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AionRpcConnectionHelper {

    private static final Logger logger = LoggerFactory.getLogger(AionRpcConnectionHelper.class);

    private GenericObjectPool<AionConnection> pool;

    @Autowired
    public AionRpcConnectionHelper(@Value("${rpc.endpoint}") String rpcEndPoint) {
        logger.info("Connection url : " + rpcEndPoint);

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

        config.setMaxTotal(100);
        config.setMaxIdle(5);
        config.setMinIdle(1);
        config.setTestOnBorrow(true);
        config.setBlockWhenExhausted(false);

        pool = new GenericObjectPool<AionConnection>(new AionRpcPoolObjectFactory(rpcEndPoint), config);

    }

    public AionConnection getConnection() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeConnection(AionConnection connection) {
        pool.returnObject(connection);
    }
}
