package org.satran.blockchain.graphql.impl.aion.service.event.rx;

import org.aion.api.IUtils;
import org.satran.blockchain.graphql.model.ContractEventFilterBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@Component
public class ContractEventHolder {

    private static Logger logger = LoggerFactory.getLogger(ContractEventHolder.class);

    private WeakHashMap<String, ContractEventPublisher> weakHashMap = new WeakHashMap<>();

    public ContractEventHolder() {

    }

    public void addEvent(String eventName, ContractEventPublisher publisher) {
        weakHashMap.put(eventName, publisher);
    }

    public ContractEventPublisher getPublisher(String eventName) {
        ContractEventPublisher publisher = weakHashMap.get(eventName);

        if(publisher != null) {
            if(publisher.isDestroyed()) {
                weakHashMap.remove(eventName);
                return null;
            } else
                return publisher;
        } else
            return null;
    }

    public boolean remove(String eventName) {
        ContractEventPublisher publisher = weakHashMap.get(eventName);

        if(publisher == null)
            return true;
        else {
            weakHashMap.remove(eventName);
            publisher.destroy();
            publisher = null;

            return true;
        }
    }

    public Collection<String> getEventNames() {
        return weakHashMap.keySet();
    }

    public void deleteAll() {
        for(Map.Entry<String, ContractEventPublisher> entry: weakHashMap.entrySet()) {
            ContractEventPublisher publisher = entry.getValue();
            publisher.destroy();
            publisher =  null;
        }

        weakHashMap.clear();

    }

    public static String hash(String contractAddress, List<String> events, ContractEventFilterBean contractEventFilterBean) {
        StringBuffer sb =  new StringBuffer();

        sb.append(contractAddress);

        if(events != null)
            events.stream().forEach(evt -> sb.append(evt));

        if(contractEventFilterBean != null) {

            List<String> addresses = contractEventFilterBean.getAddresses();
            if(addresses != null) {
                for(String address: addresses) {
                    sb.append(address);
                }
            }

            List<String> topics = contractEventFilterBean.getTopics();
            if(topics != null) {
                for(String topic: topics) {
                    sb.append(topic);
                }
            }

            long expiryTime = contractEventFilterBean.getExpireTime();
            sb.append(String.valueOf(expiryTime));

            String fromBlock = contractEventFilterBean.getFromBlock();
            if(fromBlock != null)
                sb.append(fromBlock);

            String toBlock = contractEventFilterBean.getToBlock();
            if(toBlock != null)
                sb.append(toBlock);

        }

        String eventHash = IUtils.bytes2Hex(IUtils.sha3(sb.toString().getBytes()));

        if(logger.isDebugEnabled())
            logger.debug("Calculated event hash: {}", eventHash);

        return eventHash;
    }
}