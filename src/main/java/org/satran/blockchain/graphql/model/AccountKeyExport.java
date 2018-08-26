package org.satran.blockchain.graphql.model;

import java.util.List;

public class AccountKeyExport {

    private List<String> keyfiles;
    private List<String> invalidAddr;

    public AccountKeyExport(List<String> keyfiles, List<String> invalidAddr) {
        this.keyfiles = keyfiles;
        this.invalidAddr = invalidAddr;
    }

    public List<String> getKeyfiles() {
        return keyfiles;
    }

//    public void setKeyfiles(List<String> keyfiles) {
//        this.keyfiles = keyfiles;
//    }

    public List<String> getInvalidAddr() {
        return invalidAddr;
    }

//    public void setInvalidAddr(List<String> invalidAddr) {
//        this.invalidAddr = invalidAddr;
//    }


    @Override
    public String toString() {
        return "AccountKeyExport{" +
                "keyfiles=" + keyfiles +
                ", invalidAddr=" + invalidAddr +
                '}';
    }
}
