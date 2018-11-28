package org.satran.blockchain.graphql.it.rest

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.satran.blockchain.graphql.it.TestUtil
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import spock.lang.IgnoreIf
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@IgnoreIf({ env.test_query == 'false' })
class TransactionControllerSpec extends Specification {

    @LocalServerPort
    private int port;
    private String baseUrl

    TestRestTemplate restTemplate = new TestRestTemplate();

    def setupSpec() {
        //System.setProperty("rpc.endpoint", "tcp://<host>:8547")
    }

    def "get transactions should return transaction by transaction hash"() {

        this.baseUrl = TestUtil.createRestBaseUrlWithPort(getPort())
        String url = this.baseUrl + "/v1/transactions/d2cb9814d432b6b7ca16ed2955e3410fbcfdee5ad8a79778b39d69660eb77d7c";

        when:
        ResponseEntity<String> response = restTemplate.getForEntity(url,
                String.class)

        String json = response.getBody()

        println(json)
        JsonObject jsonObject = TestUtil.getJsonObject(json)
        then:
        jsonObject.get("blockNumber").getAsLong() == 1761154
        jsonObject.get("to").getAsString().equals("a01779ba96290a3416563b4d32a7e012fa559a2f5b307b3a35dbdaf894fd55fc")
    }

    def "search transactions should return transactions by list of transaction hash"() {

        this.baseUrl = TestUtil.createRestBaseUrlWithPort(getPort())
        String url = this.baseUrl + "/v1/transactions/search?txHash=d2cb9814d432b6b7ca16ed2955e3410fbcfdee5ad8a79778b39d69660eb77d7c&txHash=c201e99641efc3151408b142547488414d28cb92a743c503646203ec3ede02b6";

        when:
        ResponseEntity<String> response = restTemplate.getForEntity(url,
                String.class)

        String json = response.getBody()

        println(json)
        JsonObject jsonObject = TestUtil.getJsonObject(json)
        JsonArray jsonArray = jsonObject.get("_embedded").getAsJsonArray("txDetailsList");
        then:
        println(jsonArray)
        jsonArray.size() == 2
        jsonArray.get(0).getAsJsonObject().get("blockNumber").getAsLong() == 1761154
        jsonArray.get(1).getAsJsonObject().get("blockNumber").getAsLong() == 1761142
        jsonArray.get(0).getAsJsonObject().get("to").getAsString().equals("a01779ba96290a3416563b4d32a7e012fa559a2f5b307b3a35dbdaf894fd55fc")
        jsonArray.get(1).getAsJsonObject().get("to").getAsString().equals("a0a03ff92a839175a144f37bd1f6dcfa656249b0bde6e5d5d23bc8ef21ccaba5")
    }

    int getPort() {
        return port
    }
}
