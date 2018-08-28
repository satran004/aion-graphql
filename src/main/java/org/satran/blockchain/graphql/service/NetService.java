package org.satran.blockchain.graphql.service;

import org.satran.blockchain.graphql.model.NetInfo;
import org.satran.blockchain.graphql.model.ProtocolInfo;

public interface NetService {

    public NetInfo getNetworkInfo();

    public boolean isSyncing();

    public ProtocolInfo getProtocol();

}
