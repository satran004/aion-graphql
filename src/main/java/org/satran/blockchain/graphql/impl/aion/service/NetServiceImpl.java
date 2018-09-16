package org.satran.blockchain.graphql.impl.aion.service;

import org.aion.api.type.Node;
import org.aion.api.type.Protocol;
import org.aion.api.type.SyncInfo;
import org.satran.blockchain.graphql.impl.aion.service.dao.AionBlockchainAccessor;
import org.satran.blockchain.graphql.exception.BlockChainAcessException;
import org.satran.blockchain.graphql.impl.aion.util.ModelConverter;
import org.satran.blockchain.graphql.model.NodeInfo;
import org.satran.blockchain.graphql.model.ProtocolInfo;
import org.satran.blockchain.graphql.model.SyncInfoBean;
import org.satran.blockchain.graphql.service.NetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class NetServiceImpl implements NetService {

    private static final Logger logger = LoggerFactory.getLogger(NetServiceImpl.class);

    private AionBlockchainAccessor accessor;

    public NetServiceImpl(AionBlockchainAccessor aionAccessFunction) {
        this.accessor = aionAccessFunction;
    }

    @Override
    public boolean isSyncing() {
        if (logger.isDebugEnabled())
            logger.debug("Getting sync info");

        return accessor.call(((apiMsg, api) -> {
            apiMsg.set(api.getNet().isSyncing());

            if (apiMsg.isError()) {
                logger.error("Unable to get sync info" + apiMsg.getErrString());
                throw new BlockChainAcessException(apiMsg.getErrString());
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
                throw new BlockChainAcessException(apiMsg.getErrString());
            }

            Protocol protocol = apiMsg.getObject();

            return ModelConverter.convert(protocol);
        }));

    }

    @Override
    public List<NodeInfo> getActiveNodes() {
        if (logger.isDebugEnabled())
            logger.debug("Getting active nodes");

        return accessor.call(((apiMsg, api) -> {
            apiMsg.set(api.getNet().getActiveNodes());

            if (apiMsg.isError()) {
                logger.error("Unable to get active nodes" + apiMsg.getErrString());
                throw new BlockChainAcessException(apiMsg.getErrString());
            }

            List<Node> nodes = apiMsg.getObject();

            if(nodes == null)
                return Collections.EMPTY_LIST;

            return ModelConverter.convert(nodes);

        }));
    }

    @Override
    public int getPeerCount() {
        if (logger.isDebugEnabled())
            logger.debug("Getting peer count");

        return accessor.call(((apiMsg, api) -> {
            apiMsg.set(api.getNet().getPeerCount());

            if (apiMsg.isError()) {
                logger.error("Unable to get peer count" + apiMsg.getErrString());
                throw new BlockChainAcessException(apiMsg.getErrString());
            }

           return apiMsg.getObject();
        }));
    }

    @Override
    public List<NodeInfo> getStaticNodes() {
        if (logger.isDebugEnabled())
            logger.debug("Getting static nodes");

        return accessor.call(((apiMsg, api) -> {
            apiMsg.set(api.getNet().getStaticNodes());

            if (apiMsg.isError()) {
                logger.error("Unable to get static nodes" + apiMsg.getErrString());
                throw new BlockChainAcessException(apiMsg.getErrString());
            }

            List<Node> nodes = apiMsg.getObject();

            if(nodes == null)
                return Collections.EMPTY_LIST;

            return ModelConverter.convert(nodes);
        }));
    }

    @Override
    public boolean isListening() {
        if (logger.isDebugEnabled())
            logger.debug("isListening()");

        return accessor.call(((apiMsg, api) -> {
            apiMsg.set(api.getNet().isListening());

            if (apiMsg.isError()) {
                logger.error("Unable to get listening status" + apiMsg.getErrString());
                throw new BlockChainAcessException(apiMsg.getErrString());
            }

            return apiMsg.getObject();
        }));
    }

    @Override
    public SyncInfoBean getSyncInfo() {
        if (logger.isDebugEnabled())
            logger.debug("Getting Sync info");

        return accessor.call(((apiMsg, api) -> {
            apiMsg.set(api.getNet().syncInfo());

            if (apiMsg.isError()) {
                logger.error("Unable to get sync info" + apiMsg.getErrString());
                throw new BlockChainAcessException(apiMsg.getErrString());
            }

            SyncInfo syncInfo = apiMsg.getObject();

            if(syncInfo == null)
                throw new BlockChainAcessException("Unable to get sync info from kernel");

            SyncInfoBean bean = new SyncInfoBean();

            bean.setChainBestBlock(syncInfo.getChainBestBlock());
            bean.setMaxImportBlocks(syncInfo.getMaxImportBlocks());
            bean.setNetworkBestBlock(syncInfo.getNetworkBestBlock());
            bean.setStartingBlock(syncInfo.getStartingBlock());
            bean.setSyncing(syncInfo.isSyncing());

            return bean;
        }));
    }
}
