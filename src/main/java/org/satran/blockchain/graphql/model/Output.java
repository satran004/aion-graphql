package org.satran.blockchain.graphql.model;

public class Output {

    public final static String HEX_TYPE = "hex";
    public final static String BASE64_TYPE = "base64";

    private String type;
    private String encoding;
//    private Boolean array;

    public Output() {

    }

    public Output(String type) {
        this.type = type;
    }

    public Output(String type, String encoding) {
        this.type = type;
        this.encoding = encoding;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public Boolean isArray() {
//        return array;
//    }
//
//    public void setArray(Boolean array) {
//        this.array = array;
//    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
