package org.satran.blockchain.graphql.it

import com.google.gson.JsonArray
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import spock.lang.IgnoreIf

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@IgnoreIf({ env.test_query == 'false' })
class BlockApiSpec extends AbstractSpec {

    @LocalServerPort
    private int port;

    private String blockQuery = """
        blockApi {
                     blocks(first: 5, before: 1496491) {
                       number
                       hash
                       parentHash
                       nrgConsumed
                       nrgLimit
                   
                     }
                 }
    """

    def setupSpec() {

        //System.setProperty("rpc.endpoint", "tcp://<host>:8547")
    }

    def "get blocks should return blocks and transactions"() {
        when:
        println(blockQuery)
        blockQuery = blockQuery.replace("\n", " ")
        ResponseEntity<String> response = invokeApi(blockQuery);
        println(response)

        JsonArray jsonArray = TestUtil.getJSONArray(response.getBody(), "blockApi", "blocks");

        then:
        println(jsonArray)
        jsonArray.size() == 5
        jsonArray.get(0).getAsJsonObject().get("number").getAsLong() != 0

    }

    @Override
    int getPort() {
        return port
    }
}
