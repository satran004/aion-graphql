package org.satran.blockchain.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.satran.blockchain.graphql.model.ContractEventBean;
import org.satran.blockchain.graphql.model.ContractEventFilterBean;
import org.satran.blockchain.graphql.model.Output;
import org.satran.blockchain.graphql.service.ContractEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Flow;

@Component
public class Subscription implements GraphQLSubscriptionResolver {

    private static Logger logger = LoggerFactory.getLogger(Subscription.class);

    @Autowired
    private ContractEventService contractEventService;

    public Publisher<ContractEventBean> registerEvents(String ownerAddress, String contractAddress, String abi, List<String> events,
                                                       ContractEventFilterBean contractEventFilterBean,
                                                       List<Output> outputs) {

        return contractEventService.registerEvents(ownerAddress,  contractAddress, abi, events, contractEventFilterBean, outputs);
    }

}
