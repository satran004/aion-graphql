package org.satran.blockchain.graphql;

import org.aion.api.IAionAPI;
import org.aion.api.IContract;
import org.aion.api.type.*;
import org.aion.base.type.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.aion.api.ITx.NRG_LIMIT_TX_MAX;
import static org.aion.api.ITx.NRG_PRICE_MIN;

public class ContractEventTest {

    private static Logger logger = LoggerFactory.getLogger(ContractEventTest.class);

    private static String url = "tcp://192.168.0.96:8548";


    public static void events(IAionAPI api, ApiMsg apiMsg) throws InterruptedException {

        Address ownerAddress = Address.wrap("a0c0cc973a306d31320fe72cad62afaa799d076bbd492ca4d5d5d941adfa12a9");
        Address contractAddress = Address.wrap("a0325401995789edf2287def289d3c6d93c9e0771b47209104cfa1b722f0c9cc");

        Address anyadd = Address.wrap("a0b851faadee3af7dcde48178036a3d683c8fe36653e10c6a49c1f7c6cb28798");
        String abiDefinition = "[{\"outputs\":[{\"name\":\"\",\"type\":\"string\"}," +
                "{\"name\":\"\",\"type\":\"uint128\"}],\"constant\":false,\"payable\":false,\"inputs\":[],\"name\"" +
                ":\"getInstructor\",\"type\":\"function\"},{\"outputs\":[],\"constant\":false,\"payable\":false,\"inputs\"" +
                ":[{\"name\":\"_fName\",\"type\":\"string\"},{\"name\":\"_age\",\"type\":\"uint128\"}],\"name\"" +
                ":\"setInstructor\",\"type\":\"function\"},{\"outputs\":[],\"inputs\":[{\"indexed\":false,\"name\":" +
                "\"name\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"age\",\"type\":\"uint128\"}],\"name\":" +
                "\"Instructor\",\"anonymous\":false,\"type\":\"event\"}]";

        IContract ctr = api.getContractController().getContractAt(anyadd, contractAddress, abiDefinition);

        // unlock account
       // api.getWallet().unlockAccount(ownerAddress, "test123");

        ctr.newEvent("Instructor");

        ContractEventFilter eventFilter = new ContractEventFilter.ContractEventFilterBuilder()
                .fromBlock("25600")
                .toBlock("25700")
//                .addresses()
                .createContractEventFilter();

        boolean ret = ctr
                .setTxNrgLimit(NRG_LIMIT_TX_MAX).setTxNrgPrice(NRG_PRICE_MIN)
                .register(eventFilter).getObject();

        System.out.println("Event registered...... : " + ret);


        //String test = "[{\"outputs\":[{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"uint128\"}],\"constant\":false,\"payable\":false,\"inputs\":[],\"name\":\"getInstructor\",\"type\":\"function\"},{\"outputs\":[],\"constant\":false,\"payable\":false,\"inputs\":[{\"name\":\"_fName\",\"type\":\"string\"},{\"name\":\"_age\",\"type\":\"uint128\"}],\"name\":\"setInstructor\",\"type\":\"function\"},{\"outputs\":[],\"inputs\":[{\"indexed\":false,\"name\":\"name\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"age\",\"type\":\"uint128\"}],\"name\":\"Instructor\",\"anonymous\":false,\"type\":\"event\"}]"
//        ctr.nonBlock().getEvents().stream().forEach(c -> {
//            System.out.println("::: " + c.getEventName() + "  " + c.getData()  + c.toString());
//            System.out.println("data: " + c.getData().toString());
//            System.out.println(String.valueOf(c.getResults().get(0)));
//            System.out.println(String.valueOf(c.getResults().get(1)));
//        });

       while(true) {

           System.out.println("Trying to get events..." + ctr.issuedEvents());
           List<ContractEvent> contractEvents = ctr.getEvents();
           contractEvents.stream().forEach(
                    c -> {
                        System.out.println("::: " + c.getEventName() + "  " + c.getData()  + c.toString());
                        System.out.println("data: " + c.getData().toString());
                        System.out.println(String.valueOf(c.getResults().get(0)));
                        System.out.println(String.valueOf(c.getResults().get(1)));
                    }
            );

           Thread.sleep(1000);

        }
//      checking that received value matches input
//        String received = Address.wrap((byte[]) rsp.getData().get(0)).toString();
//        if (!received.equals(addressToAdd)) {
//            System.out.format("The received value:%n%s%ndoes not match the given parameter:%n%s%n",
//                    received,
//                    addressToAdd);
//        } else {
//            System.out.format("The received value:%n%s%nmatches the given parameter:%n%s%n", received, addressToAdd);
//        }
    }

    public static void getNodeInfo(IAionAPI api, ApiMsg apiMsg) {

        apiMsg.set(api.getNet().getActiveNodes());

        List<Node> nodeList = apiMsg.getObject();

        nodeList.stream()
                .forEach(node -> {
                    System.out.println(node.getLatency() + "  "  +node.getP2pPort() + "  " + node.getP2pIP());
                });

        System.out.println(nodeList);
    }

    public static void main(String[] args) throws Exception {
        IAionAPI api = IAionAPI.init();

        logger.info("Trying to connect to " + url);
        ApiMsg apiMsg = api.connect(url);

        if (apiMsg.isError()) {
            logger.error("Connect server failed, exit test! " + apiMsg.getErrString());
        }

        try {

//            contractTest(api, apiMsg);
            //getNodeInfo(api, apiMsg);
//            invokeMethod(api, apiMsg);
            events(api, apiMsg);
         //   contractTest(api, apiMsg);
        } finally {
            api.destroyApi();
        }


    }
}
