package org.satran.aion.graphql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AionToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(AionToolApplication.class, args);

		/*String url = "tcp://192.168.0.96:8548";
		IAionAPI api = IAionAPI.init();
		ApiMsg apiMsg = api.connect(url);
		if (apiMsg.isError()) {
			System.out.println("Connect server failed, exit test! " + apiMsg.getErrString());
			return;
		}

		System.out.println("Get server connected!");

		apiMsg.set(api.getChain().blockNumber());
		if (apiMsg.isError()) {
			System.out.println("Get blockNumber isError: " + apiMsg.getErrString());
		}

		long bn = api.getChain().blockNumber().getObject();
		System.out.println("The highest block number is: " + bn);

		apiMsg.set(api.getAdmin().getAccountDetailsByAddressList("0xa0c0cc973a306d31320fe72cad62afaa799d076bbd492ca4d5d5d941adfa12a9"));

		if (apiMsg.isError()) {
			System.out.println("Get Account details failed !!");
			return;
		}

		AccountDetails accDtl = (AccountDetails) ((List)apiMsg.getObject()).get(0);

		System.out.println(accDtl.getBalance());


		apiMsg.set(api.getWallet().getAccounts());
		List accs = apiMsg.getObject();

		System.out.println("Total " + accs.size() + " accounts!");
		for (int i = 0; i < accs.size(); i++) {

//			accs.get(i).get
			System.out.println("Found account: " + accs.get(i).toString());
		}

		api.destroyApi();

		System.out.println("Get server connected!");*/
	}
}
