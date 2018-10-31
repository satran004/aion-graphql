package org.satran.blockchain.graphql.it;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public abstract class AbstractTest {

  TestRestTemplate restTemplate = new TestRestTemplate();

  protected ResponseEntity<String> invokeApi(String query) {

    query = "{\"query\": \"{" + query + "}\"}";

    System.out.println(query);
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<String>(query, headers);

    return restTemplate.exchange(
        TestUtil.createURLWithPort(getPort()),
        HttpMethod.POST, entity, String.class);
  }

  public abstract int getPort();

}
