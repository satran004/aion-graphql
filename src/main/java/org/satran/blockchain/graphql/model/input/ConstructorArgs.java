package org.satran.blockchain.graphql.model.input;

import java.util.List;

public class ConstructorArgs {

    private String contractName;
    private List<Param> params;

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }
}
