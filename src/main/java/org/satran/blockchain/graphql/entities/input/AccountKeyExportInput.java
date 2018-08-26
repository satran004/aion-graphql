package org.satran.blockchain.graphql.entities.input;

public class AccountKeyExportInput {

    private String publicKey;
    private String passphrase;

    public AccountKeyExportInput() {

    }

    public AccountKeyExportInput(String publicKey, String passphrase) {
        this.publicKey = publicKey;
        this.passphrase = passphrase;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }
}
