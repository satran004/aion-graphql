package org.satran.blockchain.graphql.model.input;

public class AccountKeyExportInput {

    private String address;
    private String passphrase;

    public AccountKeyExportInput() {

    }

    public AccountKeyExportInput(String address, String passphrase) {
        this.address = address;
        this.passphrase = passphrase;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }
}
