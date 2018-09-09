package org.satran.blockchain.graphql.model.input;

import org.satran.blockchain.graphql.model.Output;
import org.satran.blockchain.graphql.model.SolidityType;
import org.satran.blockchain.graphql.model.Param;

import java.util.List;

public class ContractFunction {

    private String name;
    private List<Param> params;

    private List<Output> outputs;

    public ContractFunction() {

    }

    public ContractFunction param(SolidityType type, List<Object> values) {
        Param param = new Param();
        param.setType(type);
        param.setValues(values);

        return this;
    }

    public ContractFunction param(SolidityType type, Object value) {
        Param param = new Param();
        param.setType(type);
        param.setValue(value);

        return this;
    }

    public String name() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public List<Output> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Output> outputs) {
        this.outputs = outputs;
    }

    @Override
    public String toString() {
        return "ContractFunction{" +
                "name='" + name + '\'' +
                ", params=" + params +
                '}';
    }
}

