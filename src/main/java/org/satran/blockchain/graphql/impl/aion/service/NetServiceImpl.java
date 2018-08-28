package org.satran.blockchain.graphql.impl.aion.service;

import org.aion.api.IAionAPI;
import org.aion.api.type.ApiMsg;
import org.aion.api.type.Protocol;
import org.satran.blockchain.graphql.exception.ConnectionException;
import org.satran.blockchain.graphql.impl.aion.pool.AionConnection;
import org.satran.blockchain.graphql.impl.aion.util.ModelConverter;
import org.satran.blockchain.graphql.model.NetInfo;
import org.satran.blockchain.graphql.model.ProtocolInfo;
import org.satran.blockchain.graphql.pool.ConnectionHelper;
import org.satran.blockchain.graphql.service.NetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class NetServiceImpl implements NetService {

    private static final Logger logger = LoggerFactory.getLogger(NetServiceImpl.class);

    private ConnectionHelper connectionHelper;

    public NetServiceImpl(ConnectionHelper connectionHelper) {
        this.connectionHelper = connectionHelper;
    }

    @Override
    public NetInfo getNetworkInfo() {
        NetInfo netInfo = new NetInfo();
        netInfo.setSyncing(isSyncing());
        netInfo.setProtocol(getProtocol());

        return netInfo;
    }

    @Override
    public boolean isSyncing() {
        if(logger.isDebugEnabled())
            logger.debug("Getting sync info");

        AionConnection connection = (AionConnection) connectionHelper.getConnection();

        if(connection == null)
            throw new ConnectionException("Connection could not be established");

        IAionAPI api = connection.getApi();
        ApiMsg apiMsg = connection.getApiMsg();

        try {

            apiMsg.set(api.getNet().isSyncing());
            if (apiMsg.isError()) {
                logger.error("Unable to get sync info" + apiMsg.getErrString());
                throw new RuntimeException(apiMsg.getErrString());
            }

            return apiMsg.getObject();

        } finally {
            connectionHelper.closeConnection(connection);
        }
    }

    @Override
    public ProtocolInfo getProtocol() {
        if(logger.isDebugEnabled())
            logger.debug("Getting network protocol info");

        AionConnection connection = (AionConnection) connectionHelper.getConnection();

        if(connection == null)
            throw new ConnectionException("Connection could not be established");

        IAionAPI api = connection.getApi();
        ApiMsg apiMsg = connection.getApiMsg();

        try {

            apiMsg.set(api.getNet().getProtocolVersion());
            if (apiMsg.isError()) {
                logger.error("Unable to get protocol info" + apiMsg.getErrString());
                throw new RuntimeException(apiMsg.getErrString());
            }

            Protocol protocol = apiMsg.getObject();

            return ModelConverter.convert(protocol);

        } finally {
            connectionHelper.closeConnection(connection);
        }
    }
}
