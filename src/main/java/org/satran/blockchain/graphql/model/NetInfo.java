package org.satran.blockchain.graphql.model;

public class NetInfo {
    private boolean isSyncing;
    private ProtocolInfo protocol;

    public NetInfo() {

    }

    public boolean isSyncing() {
        return isSyncing;
    }

    public void setSyncing(boolean syncing) {
        isSyncing = syncing;
    }

    public ProtocolInfo getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolInfo protocol) {
        this.protocol = protocol;
    }
}
