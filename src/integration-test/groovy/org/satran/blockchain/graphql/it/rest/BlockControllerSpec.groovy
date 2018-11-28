package org.satran.blockchain.graphql.it.rest

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.satran.blockchain.graphql.it.TestUtil
import org.satran.blockchain.graphql.model.Block
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import spock.lang.IgnoreIf
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@IgnoreIf({ env.test_query == 'false' })
class BlockControllerSpec extends Specification {

    @LocalServerPort
    private int port;
    private String baseUrl

    TestRestTemplate restTemplate = new TestRestTemplate();

    def setupSpec() {
        //System.setProperty("rpc.endpoint", "tcp://<host>:8547")
    }

    def "get blocks should return blocks and transactions"() {

        this.baseUrl = TestUtil.createRestBaseUrlWithPort(getPort())
        String url = this.baseUrl + "/v1/blocks?first=5&before=1496491";

        when:
        ResponseEntity<String> response = restTemplate.getForEntity(url,
                 String.class)

        String json = response.getBody()

        println(json)
        JsonObject jsonObject = TestUtil.getJsonObject(json)
        JsonArray jsonArray = jsonObject.get("_embedded").getAsJsonArray("blockList");
        then:
        println(jsonArray)
        jsonArray.size() == 5
        jsonArray.get(0).getAsJsonObject().get("number").getAsLong() != 0

    }

//    @Override
    int getPort() {
        return port
    }

}

