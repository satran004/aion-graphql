package org.satran.blockchain.graphql.resolvers;

import org.satran.blockchain.graphql.model.ContractEventBean;
import org.satran.blockchain.graphql.model.ContractEventFilterBean;
import org.satran.blockchain.graphql.model.ContractResponseBean;
import org.satran.blockchain.graphql.model.Output;
import org.satran.blockchain.graphql.model.input.ContractFunction;
import org.satran.blockchain.graphql.service.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContractQuery {

    private Logger logger = LoggerFactory.getLogger(ContractQuery.class);

    private ContractService contractService;

    public ContractQuery(ContractService contractService) {
        this.contractService = contractService;
    }

    public ContractResponseBean call(String fromAddress, String contractAddress, String abiDefinition,
                                     ContractFunction contractFunction) {

        return contractService.call(fromAddress, contractAddress, abiDefinition, contractFunction);
    }

    public List<ContractEventBean> events(String fromAddress, String contractAddress, String abiDefinition,
                                          List<String> events, ContractEventFilterBean eventFilterBean, List<Output> outputTypes) {

        return contractService.getContractEvents(fromAddress, contractAddress, abiDefinition, events, eventFilterBean, outputTypes);

    }
}
