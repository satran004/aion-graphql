package org.satran.blockchain.graphql.impl.aion.pool;

import org.aion.api.IAionAPI;
import org.aion.api.type.ApiMsg;
import org.satran.blockchain.graphql.pool.ChainConnection;
import org.satran.blockchain.graphql.pool.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AionConnectionFactory implements ConnectionFactory {

    private final static Logger logger = LoggerFactory.getLogger(AionConnectionFactory.class);

    @Value("${rpc.endpoint}")
    String rpcEndPoint;

    @Override
    public ChainConnection createConnection() {
        IAionAPI api = IAionAPI.init();

        logger.info("Trying to connect to " + rpcEndPoint);
        ApiMsg apiMsg = api.connect(rpcEndPoint);

        if (apiMsg.isError()) {
            logger.error("Connect server failed, exit test! " + apiMsg.getErrString());
            return null;
        } else {
            return new AionConnection(api, apiMsg);
        }
    }
}
