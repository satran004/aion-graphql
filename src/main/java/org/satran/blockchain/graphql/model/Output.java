package org.satran.blockchain.graphql.model;

public class Output {

    private SolidityType type;
    private Boolean array;

    public SolidityType getType() {
        return type;
    }

    public void setType(SolidityType type) {
        this.type = type;
    }

    public Boolean isArray() {
        return array;
    }

    public void setArray(Boolean array) {
        this.array = array;
    }
}
