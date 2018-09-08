package org.satran.blockchain.graphql.model;

import java.util.List;

public class SolidityArgBean {

    private boolean isDynamic;
    private int dynamicOffset;
    private String inputFormat;
    private List<Integer> dynamicParameters;
    private String type;

    public boolean isDynamic() {
        return isDynamic;
    }

    public void setDynamic(boolean dynamic) {
        isDynamic = dynamic;
    }

    public int getDynamicOffset() {
        return dynamicOffset;
    }

    public void setDynamicOffset(int dynamicOffset) {
        this.dynamicOffset = dynamicOffset;
    }

    public String getInputFormat() {
        return inputFormat;
    }

    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
    }

    public List<Integer> getDynamicParameters() {
        return dynamicParameters;
    }

    public void setDynamicParameters(List<Integer> dynamicParameters) {
        this.dynamicParameters = dynamicParameters;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
