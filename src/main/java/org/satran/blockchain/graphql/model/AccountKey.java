package org.satran.blockchain.graphql.model;

public class AccountKey {

    private String passphrase;
    private String publicKey;
    private String privateKey;

    public AccountKey(String passphrase, String publicKey, String privateKey) {
        this.passphrase = passphrase;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

}
