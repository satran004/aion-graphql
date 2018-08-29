package org.satran.blockchain.graphql.impl.aion.service;

import org.aion.api.type.Protocol;
import org.satran.blockchain.graphql.impl.aion.util.ModelConverter;
import org.satran.blockchain.graphql.model.NetInfo;
import org.satran.blockchain.graphql.model.ProtocolInfo;
import org.satran.blockchain.graphql.service.NetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class NetServiceImpl implements NetService {

    private static final Logger logger = LoggerFactory.getLogger(NetServiceImpl.class);

    private AionBlockchainAccessor accessor;

    public NetServiceImpl(AionBlockchainAccessor aionAccessFunction) {
        this.accessor = aionAccessFunction;
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
        if (logger.isDebugEnabled())
            logger.debug("Getting sync info");

        return accessor.call(((apiMsg, api) -> {
            apiMsg.set(api.getNet().isSyncing());

            if (apiMsg.isError()) {
                logger.error("Unable to get sync info" + apiMsg.getErrString());
                throw new RuntimeException(apiMsg.getErrString());
            }

            return apiMsg.getObject();
        }));

    }

    @Override
    public ProtocolInfo getProtocol() {
        if (logger.isDebugEnabled())
            logger.debug("Getting network protocol info");

        return accessor.call(((apiMsg, api) -> {
            apiMsg.set(api.getNet().getProtocolVersion());

            if (apiMsg.isError()) {
                logger.error("Unable to get protocol info" + apiMsg.getErrString());
                throw new RuntimeException(apiMsg.getErrString());
            }

            Protocol protocol = apiMsg.getObject();

            return ModelConverter.convert(protocol);
        }));

    }
}
