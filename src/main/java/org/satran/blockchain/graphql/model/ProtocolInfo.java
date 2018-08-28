package org.satran.blockchain.graphql.model;

public class ProtocolInfo {
    private String api;
    private String db;
    private String kernel;
    private String miner;
    private String net;
    private String txpool;
    private String vm;

    public ProtocolInfo() {

    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getKernel() {
        return kernel;
    }

    public void setKernel(String kernel) {
        this.kernel = kernel;
    }

    public String getMiner() {
        return miner;
    }

    public void setMiner(String miner) {
        this.miner = miner;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getTxpool() {
        return txpool;
    }

    public void setTxpool(String txpool) {
        this.txpool = txpool;
    }

    public String getVm() {
        return vm;
    }

    public void setVm(String vm) {
        this.vm = vm;
    }
}
