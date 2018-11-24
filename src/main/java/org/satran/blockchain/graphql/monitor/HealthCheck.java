package org.satran.blockchain.graphql.monitor;

import org.satran.blockchain.graphql.model.Block;
import org.satran.blockchain.graphql.service.BlockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class HealthCheck implements HealthIndicator{

    private final static Logger logger = LoggerFactory.getLogger(HealthCheck.class);

    @Autowired
    BlockService blockService;

    @Override
    public Health health() {
        int errorCode = check(); // perform some specific health check
        if (errorCode != 0) {
            return Health.down()
                .withDetail("Error Code", errorCode).build();
        }
        return Health.up().build();
    }

    public int check() {
        // Our logic to check health
        return checkGetBlocks();
    }

    private int checkGetBlocks() {

        try {
            Block block = blockService.getLatestBlock();

            if (block != null)
                return 0;
            else
                return -1;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }


}
