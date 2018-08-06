package org.satran.aion.graphql.pool;

import org.aion.api.IAionAPI;
import org.aion.api.type.ApiMsg;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AionRpcPoolObjectFactory extends BasePooledObjectFactory<org.satran.aion.graphql.pool.AionConnection> {

    private static final Logger logger = LoggerFactory.getLogger(AionRpcPoolObjectFactory.class);

    private String connectUrl;

    public AionRpcPoolObjectFactory(String connectUrl) {
        this.connectUrl = connectUrl;
    }

    public AionConnection create() throws Exception {
        IAionAPI api = IAionAPI.init();

        logger.info("Trying to connect to " + connectUrl);
        ApiMsg apiMsg = api.connect(connectUrl);

        if (apiMsg.isError()) {
            logger.error("Connect server failed, exit test! " + apiMsg.getErrString());
            return null;
        } else {
            return new org.satran.aion.graphql.pool.AionConnection(api, apiMsg);
        }
    }

    public PooledObject<AionConnection> wrap(AionConnection aionConnection) {
        return new DefaultPooledObject<AionConnection>(aionConnection);
    }

    @Override
    public void destroyObject(PooledObject<AionConnection> p) throws Exception {
        AionConnection aionConnection = p.getObject();

        if(aionConnection != null) {
            aionConnection.destroy();
        }
    }

    @Override
    public boolean validateObject(PooledObject<AionConnection> p) {
        if(p.getObject() != null)
            return p.getObject().getApi().isConnected();
        else
            return super.validateObject(p);
    }
}
