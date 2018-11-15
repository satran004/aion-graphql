package org.satran.blockchain.graphql.it.rest;

import org.satran.blockchain.graphql.it.TestUtil;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import spock.lang.Specification;

abstract class AbstractControllerSpec extends Specification {
    TestRestTemplate restTemplate = new TestRestTemplate();

    public abstract int getPort();
}
