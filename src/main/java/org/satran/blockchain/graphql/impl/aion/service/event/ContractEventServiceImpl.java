package org.satran.blockchain.graphql.impl.aion.service.event;

import org.aion.api.IAionAPI;
import org.aion.api.IContract;
import org.aion.api.type.ApiMsg;
import org.aion.base.type.Address;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.satran.blockchain.graphql.exception.BlockChainAcessException;
import org.satran.blockchain.graphql.impl.aion.pool.AionConnection;
import org.satran.blockchain.graphql.impl.aion.service.ContractServiceImpl;
import org.satran.blockchain.graphql.impl.aion.service.event.rx.ContractEventHolder;
import org.satran.blockchain.graphql.impl.aion.service.event.rx.ContractEventPublisher;
import org.satran.blockchain.graphql.impl.aion.util.ModelConverter;
import org.satran.blockchain.graphql.model.ContractEventBean;
import org.satran.blockchain.graphql.model.ContractEventFilterBean;
import org.satran.blockchain.graphql.model.Output;
import org.satran.blockchain.graphql.pool.ConnectionHelper;
import org.satran.blockchain.graphql.service.ContractEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.aion.api.ITx.NRG_LIMIT_TX_MAX;
import static org.aion.api.ITx.NRG_PRICE_MIN;

@Repository
public class ContractEventServiceImpl implements ContractEventService {

    private static final Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);

    @Autowired
    ConnectionHelper connectionHelper;

    @Autowired
    ContractEventHolder contractEventHolder;

    @Override
    public Publisher<ContractEventBean> registerEvents(String ownerAddressStr, String contractAddressStr, String abi, List<String> events,
                                                       ContractEventFilterBean contractEventFilterBean, List<Output> outputTypes) {

        Address contractAddress = Address.wrap(contractAddressStr);

        Address ownerAddress = null; //Owner address is not mandatory. Can be filled with contract adress
        if(StringUtils.isEmpty(ownerAddressStr))
            ownerAddress = contractAddress;
        else
            ownerAddress = Address.wrap(ownerAddressStr);

        AionConnection aionConnection = (AionConnection) connectionHelper.getConnection();

        IAionAPI api = aionConnection.getApi();
        ApiMsg apiMsg = aionConnection.getApiMsg();

        IContract ctr = api.getContractController().getContractAt(ownerAddress, contractAddress, abi);

        ctr.newEvents(events);

        ctr.setTxNrgLimit(NRG_LIMIT_TX_MAX).setTxNrgPrice(NRG_PRICE_MIN)
                .newEvents(events);

        ApiMsg apiMsg1;
        if (contractEventFilterBean != null)
            apiMsg1 = ctr.register(ModelConverter.convert(contractEventFilterBean));
        else
            apiMsg1 = ctr.register();

        if(apiMsg1.isError()) {
            logger.error("Error registering event. {} : {} ", apiMsg1.getErrorCode(), apiMsg1.getErrString());
            throw new BlockChainAcessException(apiMsg1.getErrorCode(), apiMsg1.getErrString());
        }

        String eventHash = ContractEventHolder.hash(contractAddressStr, events, contractEventFilterBean);

        ContractEventPublisher contractEventPublisher = contractEventHolder.getPublisher(eventHash);

        if(contractEventPublisher == null) {
            logger.debug("There is no event publisher for event {} . Create a new one.", eventHash);
            contractEventPublisher = new ContractEventPublisher(aionConnection, ctr, outputTypes);

            //add the publisher to holder
            contractEventHolder.addEvent(eventHash, contractEventPublisher);
        } else
            logger.debug("Found an existing publisher for {}", eventHash);


        return contractEventPublisher.getPublisher();
    }

}
