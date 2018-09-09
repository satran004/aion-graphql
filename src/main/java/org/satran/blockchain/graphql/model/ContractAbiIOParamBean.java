package org.satran.blockchain.graphql.model;

import java.util.List;

public class ContractAbiIOParamBean {
    private String name;
    private List<Integer> paramLengths;
    private String type;
    private boolean indexed;

    public ContractAbiIOParamBean(String name, List<Integer> paramLengths, String type, boolean indexed) {
        this.name = name;
        this.paramLengths = paramLengths;
        this.type = type;
        this.indexed = indexed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getParamLengths() {
        return paramLengths;
    }

    public void setParamLengths(List<Integer> paramLengths) {
        this.paramLengths = paramLengths;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isIndexed() {
        return indexed;
    }

    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }
}