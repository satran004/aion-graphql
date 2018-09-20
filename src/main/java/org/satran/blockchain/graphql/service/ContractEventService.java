package org.satran.blockchain.graphql.service;

import org.reactivestreams.Publisher;
import org.satran.blockchain.graphql.model.ContractEventBean;
import org.satran.blockchain.graphql.model.ContractEventFilterBean;
import org.satran.blockchain.graphql.model.Output;

import java.util.List;

public interface ContractEventService {

    Publisher<ContractEventBean> registerEvents(String ownerAddress, String contractAddress, String abi, List<String> events,
                                                ContractEventFilterBean contractEventFilterBean, List<Output> outputTypes);

}
