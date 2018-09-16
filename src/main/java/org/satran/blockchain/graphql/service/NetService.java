package org.satran.blockchain.graphql.service;

import org.satran.blockchain.graphql.model.NetInfo;
import org.satran.blockchain.graphql.model.NodeInfo;
import org.satran.blockchain.graphql.model.ProtocolInfo;
import org.satran.blockchain.graphql.model.SyncInfoBean;

import java.util.List;

public interface NetService {

    public boolean isSyncing();

    public ProtocolInfo getProtocol();

    public List<NodeInfo> getActiveNodes();

    public int getPeerCount();

    public List<NodeInfo> getStaticNodes();

    public boolean isListening();

    public SyncInfoBean getSyncInfo();
}
