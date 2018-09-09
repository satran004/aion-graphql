package org.satran.blockchain.graphql.model;

import java.util.List;

public class ContractAbiEntryBean {
    private boolean anonymous;
    private boolean constant;
    private List<ContractAbiIOParamBean> inputs;
    private String name;
    private List<ContractAbiIOParamBean> outputs;
    private boolean payable;
    private String type;

    private boolean event;
    private boolean constructor;
    private String hashed;
    private boolean fallback;


    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public boolean isConstant() {
        return constant;
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    public List<ContractAbiIOParamBean> getInputs() {
        return inputs;
    }

    public void setInputs(List<ContractAbiIOParamBean> inputs) {
        this.inputs = inputs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ContractAbiIOParamBean> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<ContractAbiIOParamBean> outputs) {
        this.outputs = outputs;
    }

    public boolean isPayable() {
        return payable;
    }

    public void setPayable(boolean payable) {
        this.payable = payable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEvent() {
        return event;
    }

    public void setEvent(boolean event) {
        this.event = event;
    }

    public boolean isConstructor() {
        return constructor;
    }

    public void setConstructor(boolean constructor) {
        this.constructor = constructor;
    }

    public String getHashed() {
        return hashed;
    }

    public void setHashed(String hashed) {
        this.hashed = hashed;
    }

    public boolean isFallback() {
        return fallback;
    }

    public void setFallback(boolean fallback) {
        this.fallback = fallback;
    }
}
