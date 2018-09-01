package org.satran.blockchain.graphql.resolvers;

import org.satran.blockchain.graphql.exception.DataFetchingException;
import org.satran.blockchain.graphql.model.NetInfo;
import org.satran.blockchain.graphql.model.ProtocolInfo;
import org.satran.blockchain.graphql.service.NetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NetInfoResolver {

    private static Logger logger = LoggerFactory.getLogger(NetInfoResolver.class);

    private NetService netService;

    public NetInfoResolver(NetService netService) {
        this.netService = netService;
    }

    public boolean isSyncing() {
        try {
            return netService.isSyncing();
        } catch (Exception e) {
            logger.error("Error getting syncing info");
            throw new DataFetchingException(e.getMessage());
        }
    }

    public ProtocolInfo protocol() {
        try {
            return netService.getProtocol();
        } catch (Exception e) {
            logger.error("Error getting protocol info");
            throw new DataFetchingException(e.getMessage());
        }
    }

}
