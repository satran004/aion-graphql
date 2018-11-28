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
@IgnoreIf({ env.test_mutation != 'true' })
class TransactionMutationSpec extends AbstractSpec {

    @LocalServerPort
    private int port;

    private String unlockAccountInput = """
        mutation{
         walletApi {
            unlockAccount(address: \\"a0c0cc973a306d31320fe72cad62afaa799d076bbd492ca4d5d5d941adfa12a9\\"
            passphrase: \\"test123\\"
            ) 
          }
          }
    """

    private String contractDeployInput = """
        mutation {
         txnApi {
            contractDeploy(contractDeploy: {
              code: \\"contract Mortal { /* Define variable owner of the type address */ address owner; /* This function is executed at initialization and sets the owner of the contract */ function Mortal() { owner = msg.sender; } /* Function to recover the funds on the contract */ function kill() { if (msg.sender == owner) selfdestruct(owner); } } contract Greeter is Mortal { /* Define variable greeting of the type string */ string greeting; /* This runs when the contract is executed */ function Greeter(string _greeting) public { greeting = _greeting; } /* Main function */ function greet() constant returns (string) { return greeting; } }\\"
              from: \\"a0c0cc973a306d31320fe72cad62afaa799d076bbd492ca4d5d5d941adfa12a9\\"
              nrgPrice: 10000000000
              nrgLimit: 5000000
              value: 0 
            } ) {
              address
              txId
            }
          }
          }
    """
    def setupSpec() {
        System.setProperty("rpc.endpoint", "tcp://<host>:8548")
    }

    def "unlock should return true"() {
        when:
        unlockAccountInput = unlockAccountInput.replace("\n", " ")

        println(unlockAccountInput)
        ResponseEntity<String> response = executionApi(unlockAccountInput)
        println(response)

        String ret = TestUtil.getStringValue(response.getBody(), "walletApi", "unlockAccount");

        then:
        "true".equals(ret)
    }

    def "contract deploy should be succesful and returns address and txId"() {
        when:
        contractDeployInput = contractDeployInput.replace("\n", " ")

        println(contractDeployInput)
        ResponseEntity<String> response = executionApi(contractDeployInput)
        println(response)

        JsonArray jsonArray = TestUtil.getJSONArray(response.getBody(), "txnApi", "contractDeploy");

        then:
        jsonArray.size() == 2
        jsonArray.get(0).getAsJsonObject().get("address").getAsString().startsWith("a0")
        jsonArray.get(0).getAsJsonObject().get("txId").getAsString().length() > 2
        jsonArray.get(1).getAsJsonObject().get("address").getAsString().startsWith("a0")
        jsonArray.get(1).getAsJsonObject().get("txId").getAsString().length() > 2

    }

    @Override
    int getPort() {
        return port
    }
}
