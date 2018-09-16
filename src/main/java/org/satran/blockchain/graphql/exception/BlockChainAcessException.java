package org.satran.blockchain.graphql.exception;

public class BlockChainAcessException extends RuntimeException {

    public BlockChainAcessException(String msg) {
        super(msg);
    }

    public BlockChainAcessException(int errorCode, String errorMsg) {
        super(String.format("Code: %s, Error: %s", errorCode, errorMsg));
    }

    public BlockChainAcessException(String msg, Exception e) {
        super(msg, e);
    }
}
