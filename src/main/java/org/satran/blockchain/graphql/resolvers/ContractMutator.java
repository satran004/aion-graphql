package org.satran.blockchain.graphql.resolvers;

import org.satran.blockchain.graphql.model.ContractBean;
import org.satran.blockchain.graphql.model.ContractResponseBean;
import org.satran.blockchain.graphql.model.input.ContractFunction;
import org.satran.blockchain.graphql.service.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class ContractMutator {

    private Logger logger = LoggerFactory.getLogger(ContractMutator.class);

    private ContractService contractService;

    public ContractMutator(ContractService contractService) {
        this.contractService = contractService;
    }

    public List<ContractBean> createFromSource(String source, String from, long nrgLimit, long nrgPrice, BigInteger value) {

        if(value == null)
            return contractService.createFromSource​(source, from, nrgLimit, nrgPrice);
        else
            return contractService.createFromSource​(source, from, nrgLimit, nrgPrice, value);
    }

    public ContractResponseBean execute(String fromAddress, String contractAddress, String abiDefinition,
                                        ContractFunction contractFunction, long nrgLimit, long nrgPrice, long txValue) {

        if(logger.isDebugEnabled()) {
            logger.debug("From address: {}", fromAddress);
            logger.debug("Contract Address: {}" + contractAddress);
            logger.debug("Abi Definition: {}" + abiDefinition);

            logger.debug("Contract function: {}" + contractFunction);
        }

        return contractService.execute(fromAddress, contractAddress, abiDefinition, contractFunction, nrgLimit, nrgPrice, txValue);

    }
}
