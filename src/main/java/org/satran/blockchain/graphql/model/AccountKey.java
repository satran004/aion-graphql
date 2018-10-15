package org.satran.blockchain.graphql.model;

public class AccountKey {

    private String passphrase;
    private String address;
    private String privateKey;

    public AccountKey(String passphrase, String address, String privateKey) {
        this.passphrase = passphrase;
        this.address = address;
        this.privateKey = privateKey;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

}
