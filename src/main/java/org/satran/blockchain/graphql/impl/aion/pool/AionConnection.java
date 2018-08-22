package org.satran.blockchain.graphql.impl.aion.pool;

import org.aion.api.IAionAPI;
import org.aion.api.type.ApiMsg;
import org.satran.blockchain.graphql.pool.ChainConnection;
import org.springframework.stereotype.Component;

public class AionConnection implements ChainConnection {

    private IAionAPI api;
    private ApiMsg apiMsg;

    public AionConnection(IAionAPI api, ApiMsg apiMsg) {
        this.api = api;
        this.apiMsg = apiMsg;
    }

    public IAionAPI getApi() {
        return api;
    }

    public void setApi(IAionAPI api) {
        this.api = api;
    }

    public ApiMsg getApiMsg() {
        return apiMsg;
    }

    public void setApiMsg(ApiMsg apiMsg) {
        this.apiMsg = apiMsg;
    }

    @Override
    public boolean validate() {
        if(api != null)
            return api.isConnected();
        else
            return false;
    }

    @Override
    public void destroy() {
        if(api != null) {
            api.destroyApi();
        }

        api = null;
        apiMsg = null;
    }
}
