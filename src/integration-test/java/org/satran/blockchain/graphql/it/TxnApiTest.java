package org.satran.blockchain.graphql.it;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TxnApiTest {

  @LocalServerPort
  private int port;

  @Value("${transactions.query}")
  private String txnQuery;

  TestRestTemplate restTemplate = new TestRestTemplate();

  @Test
  public void getTransactionsTest() throws Exception {
    System.out.println(txnQuery);

    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<String>(txnQuery, headers);

    ResponseEntity<String> response = restTemplate.exchange(
        TestUtil.createURLWithPort(port),
        HttpMethod.POST, entity, String.class);

    System.out.printf(response.toString() );

    JsonArray jsonArray = TestUtil.getJSONArray(response.getBody(), "txnApi", "transactions");

    assertThat(jsonArray.size(), equalTo(3));
    assertThat(jsonArray.get(0).getAsJsonObject().get("to"), is(notNullValue()));
    assertThat(jsonArray.get(0).getAsJsonObject().get("to").getAsString(), is(startsWith("a0")));

    assertThat(jsonArray.get(0).getAsJsonObject().get("from"), is(notNullValue()));
    assertThat(jsonArray.get(0).getAsJsonObject().get("from").getAsString(), is(startsWith("a0")));

    assertThat(jsonArray.get(0).getAsJsonObject().get("timestamp").getAsLong(), is(not(equalTo(0))));
  }




}

