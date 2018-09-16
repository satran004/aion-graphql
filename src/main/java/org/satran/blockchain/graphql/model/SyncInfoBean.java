package org.satran.blockchain.graphql.model;

public class SyncInfoBean {

    private long chainBestBlock;
    private long maxImportBlocks;
    private long networkBestBlock;
    private long startingBlock;
    private boolean isSyncing;

    public long getChainBestBlock() {
        return chainBestBlock;
    }

    public void setChainBestBlock(long chainBestBlock) {
        this.chainBestBlock = chainBestBlock;
    }

    public long getMaxImportBlocks() {
        return maxImportBlocks;
    }

    public void setMaxImportBlocks(long maxImportBlocks) {
        this.maxImportBlocks = maxImportBlocks;
    }

    public long getNetworkBestBlock() {
        return networkBestBlock;
    }

    public void setNetworkBestBlock(long networkBestBlock) {
        this.networkBestBlock = networkBestBlock;
    }

    public long getStartingBlock() {
        return startingBlock;
    }

    public void setStartingBlock(long startingBlock) {
        this.startingBlock = startingBlock;
    }

    public boolean isSyncing() {
        return isSyncing;
    }

    public void setSyncing(boolean syncing) {
        isSyncing = syncing;
    }
}
