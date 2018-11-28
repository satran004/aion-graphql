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
import org.junit.Ignore;
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
public class TxnApiTest extends AbstractTest {

  @LocalServerPort
  private int port;

  @Value("${txnApi.transactions}")
  private String txnsQuery;

  @Value("${txnApi.transaction}")
  private String txnQuery;

  @Value("${txnApi.nrgPrice}")
  private String nrgPriceQuery;

  @Value("${txnApi.solcVersion}")
  private String solcVersionQuery;

  @Test
  public void getTransactionsTest() throws Exception {

    if(isIgnore())
      return;

    System.out.println(txnsQuery);

    ResponseEntity<String> response = invokeApi(txnsQuery);

    System.out.printf(response.toString() );

    JsonArray jsonArray = TestUtil.getJSONArray(response.getBody(), "txnApi", "transactions");

    assertThat(jsonArray.size(), equalTo(3));
    assertThat(jsonArray.get(0).getAsJsonObject().get("to"), is(notNullValue()));
    assertThat(jsonArray.get(0).getAsJsonObject().get("to").getAsString(), is(startsWith("a0")));

    assertThat(jsonArray.get(0).getAsJsonObject().get("from"), is(notNullValue()));
    assertThat(jsonArray.get(0).getAsJsonObject().get("from").getAsString(), is(startsWith("a0")));

    assertThat(jsonArray.get(0).getAsJsonObject().get("timestamp").getAsLong(), is(not(equalTo(0))));
  }

  @Test
  @Ignore
  public void getTransactionTest() throws Exception {
    if(isIgnore())
      return;

    System.out.println(txnQuery);
    ResponseEntity<String> response = invokeApi(txnQuery);

    System.out.printf(response.toString() );

    JsonObject jsonObject = TestUtil.getJsonObject(response.getBody(), "txnApi", "transaction");

    assertThat(jsonObject.get("to").getAsString(), equalTo("a0b017c28ad66e3a94a2618475b26b30b5fb02bb4e1ca0df1b77c10f1e9e0557"));
    assertThat(jsonObject.get("from").getAsString(), equalTo("a022a68ef27e5febe4570edb2ce5586974cb326f24fce2ebb23012c07dac90e0"));

    assertThat(jsonObject.get("value").getAsString(), equalTo("1535782199309000000"));
    assertThat(jsonObject.get("blockNumber").getAsLong(), equalTo(1562776));

  }

  @Test
  public void getNrgPriceTest() throws Exception {
    if(isIgnore())
      return;

    ResponseEntity<String> response = invokeApi(nrgPriceQuery);

    System.out.printf(response.toString() );

    String result = TestUtil.getStringValue(response.getBody(), "txnApi", "nrgPrice");

    long value = Long.valueOf(result);
    assertThat(value, is(not(equalTo(0))));
  }

  @Test
  public void getSolcVersionTest() throws Exception {
    if(isIgnore())
      return;

    ResponseEntity<String> response = invokeApi(solcVersionQuery);

    System.out.printf(response.toString() );

    String result = TestUtil.getStringValue(response.getBody(), "txnApi", "solcVersion");

    assertThat(result, is(notNullValue()));
  }

  public int getPort() {
    return port;
  }

}

