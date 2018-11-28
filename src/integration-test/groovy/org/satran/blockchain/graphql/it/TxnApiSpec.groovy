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
class TxnApiSpec extends AbstractSpec {

    @LocalServerPort
    private int port;

    private String txnsByHashQuery = """
        txnApi {
            transactionsByHash(txHash: [
              \\"d2cb9814d432b6b7ca16ed2955e3410fbcfdee5ad8a79778b39d69660eb77d7c\\",
              \\"c201e99641efc3151408b142547488414d28cb92a743c503646203ec3ede02b6\\",
             \\"f5eb211f75c579b5d2e2e9975d8802a5bf1adcd84c478420d169a9be3635612a\\",
              \\"66c11cdf65422aa781c81e65027d8fc42323e877ef4ad97782385863af17e953\\"
            ]) {
              blockNumber
              to
              from
              value
              txHash
            }
        }
    """

    def setupSpec() {

        //System.setProperty("rpc.endpoint", "tcp://<host>:8547")
    }

    def "get transactionsByHash should return transactions for given txn hashes"() {
        when:
        txnsByHashQuery = txnsByHashQuery.replace("\n", " ")
        ResponseEntity<String> response = invokeApi(txnsByHashQuery);
        println(response)

        JsonArray jsonArray = TestUtil.getJSONArray(response.getBody(), "txnApi", "transactionsByHash");

        then:
        println(jsonArray)
        jsonArray.size() == 4
        jsonArray.get(0).getAsJsonObject().get("blockNumber").getAsLong() == 1761154
        jsonArray.get(1).getAsJsonObject().get("blockNumber").getAsLong() == 1761142
        jsonArray.get(2).getAsJsonObject().get("blockNumber").getAsLong() == 1761142
        jsonArray.get(3).getAsJsonObject().get("blockNumber").getAsLong() == 1761144

        jsonArray.get(0).getAsJsonObject().get("to").getAsString().equals("a01779ba96290a3416563b4d32a7e012fa559a2f5b307b3a35dbdaf894fd55fc")
        jsonArray.get(1).getAsJsonObject().get("to").getAsString().equals("a0a03ff92a839175a144f37bd1f6dcfa656249b0bde6e5d5d23bc8ef21ccaba5")
        jsonArray.get(2).getAsJsonObject().get("to").getAsString().equals("a031949a9a11d739f82385778cd01395839b362d8f19ec060dc188f515d27201")
        jsonArray.get(3).getAsJsonObject().get("to").getAsString().equals("a06c9034f368e04b32e3f739ee155e5d19c4d223a527deb8e99b4d5f8e4e383c")

    }

    @Override
    int getPort() {
        return port
    }

}
