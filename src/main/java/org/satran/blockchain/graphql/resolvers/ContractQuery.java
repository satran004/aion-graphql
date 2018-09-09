package org.satran.blockchain.graphql.resolvers;

import org.satran.blockchain.graphql.model.ContractResponseBean;
import org.satran.blockchain.graphql.model.input.ContractFunction;
import org.satran.blockchain.graphql.service.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
}
