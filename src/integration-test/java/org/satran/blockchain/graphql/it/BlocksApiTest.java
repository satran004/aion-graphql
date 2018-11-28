package org.satran.blockchain.graphql.it;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
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
@Ignore()
public class BlocksApiTest extends AbstractTest {

    @LocalServerPort
    private int port;

    @Value("${blocks.query}")
    private String blocksQuery;

    @Test
    public void getBlocksTest() throws Exception {

        if(isIgnore())
            return;

        System.out.println(blocksQuery);

        ResponseEntity<String> response = invokeApi(blocksQuery);

        System.out.printf(response.toString() );

        JsonArray jsonArray = TestUtil.getJSONArray(response.getBody(), "blockApi", "blocks");

        assertThat(jsonArray.size(), equalTo(5));
        assertThat(jsonArray.get(0).getAsJsonObject().get("number").getAsLong(), is(not(equalTo(0))));
        assertThat(jsonArray.get(0).getAsJsonObject().get("hash").getAsString(), is(notNullValue()));

        assertThat(jsonArray.get(0).getAsJsonObject().get("nrgConsumed").getAsLong(), is(not(equalTo(0))));

        assertThat(jsonArray.get(0).getAsJsonObject().getAsJsonArray("txDetails").size(), is(greaterThan(0)));

        assertThat(jsonArray.get(0).getAsJsonObject().getAsJsonArray("txDetails")
            .get(0).getAsJsonObject().get("timestamp").getAsLong(), is(not(equalTo(0))));

        JsonObject txnObj = jsonArray.get(0).getAsJsonObject().getAsJsonArray("txDetails")
            .get(0).getAsJsonObject();

    }

    public int getPort() {
        return port;
    }
}
