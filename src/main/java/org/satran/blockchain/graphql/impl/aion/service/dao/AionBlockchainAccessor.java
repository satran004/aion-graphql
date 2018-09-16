package org.satran.blockchain.graphql.impl.aion.service.dao;

import org.aion.api.IAionAPI;
import org.aion.api.type.ApiMsg;
import org.satran.blockchain.graphql.exception.BlockChainAcessException;
import org.satran.blockchain.graphql.exception.ConnectionException;
import org.satran.blockchain.graphql.impl.aion.pool.AionConnection;
import org.satran.blockchain.graphql.pool.ConnectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AionBlockchainAccessor {

    private static Logger logger = LoggerFactory.getLogger(AionBlockchainAccessor.class);
    private final ConnectionHelper connectionHelper;

    public AionBlockchainAccessor(ConnectionHelper connectionHelper) {
        this.connectionHelper = connectionHelper;
    }

    public <Any> Any call(AionAccessFunction<Any> aionAccessFunction) {
        AionConnection connection = (AionConnection) connectionHelper.getConnection();

        if(connection == null)
            throw new ConnectionException("Connection could not be established");

        IAionAPI api = connection.getApi();
        ApiMsg apiMsg = connection.getApiMsg();

        try {

           return aionAccessFunction.invoke(apiMsg, api);

        } catch(BlockChainAcessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error invoking method", e);
            throw new BlockChainAcessException(apiMsg.getErrorCode(), apiMsg.getErrString());
        } finally {
            connectionHelper.closeConnection(connection);
        }
    }
}
