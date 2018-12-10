package org.satran.blockchain.graphql.model.input;

import java.util.List;

public class Param {

    private String type;
    private List<Object> values;
    private Object value;
    //Only used for bytes type for encoding and decoding
    private String encoding;

//    public SolidityType getType() {
//        return type;
//    }

//    public void setType(SolidityType type) {
//        this.type = type;
//    }

    public Param() {

    }

    public Param(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Param(String type, List<Object> values) {
        this.type = type;
        this.values = values;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
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
