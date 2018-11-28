package org.satran.blockchain.graphql.it

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import spock.lang.Specification

abstract class AbstractSpec extends Specification {

    TestRestTemplate restTemplate = new TestRestTemplate();

    protected ResponseEntity<String> invokeApi(String query) {

        query = "{\"query\": \"{" + query + "}\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON)
        HttpEntity<String> entity = new HttpEntity<String>(query, headers);

        println(TestUtil.createGraphQLURLWithPort(getPort()))
        return restTemplate.exchange(
                TestUtil.createGraphQLURLWithPort(getPort()),
                HttpMethod.POST, entity, String.class);
    }

    protected ResponseEntity<String> executionApi(String query) {

        query = "{\"query\": \"" + query + "\"}";

        println(query)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON)
        HttpEntity<String> entity = new HttpEntity<String>(query, headers);

        println(TestUtil.createGraphQLURLWithPort(getPort()))
        return restTemplate.exchange(
                TestUtil.createGraphQLURLWithPort(getPort()),
                HttpMethod.POST, entity, String.class);
    }

    public abstract int getPort();
}
