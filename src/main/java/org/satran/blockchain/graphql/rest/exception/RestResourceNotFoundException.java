package org.satran.blockchain.graphql.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RestResourceNotFoundException extends RuntimeException {

    public RestResourceNotFoundException(String msg) {
        super(msg);
    }
}
