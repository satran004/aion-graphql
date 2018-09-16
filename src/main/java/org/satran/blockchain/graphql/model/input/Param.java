package org.satran.blockchain.graphql.model.input;

import org.satran.blockchain.graphql.model.SolidityType;

import java.util.List;

public class Param {

    private SolidityType type;
    private List<Object> values;
    private Object value;

    public SolidityType getType() {
        return type;
    }

    public void setType(SolidityType type) {
        this.type = type;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Param{" +
                "type=" + type +
                ", values=" + values +
                ", value='" + value + '\'' +
                '}';
    }
}
