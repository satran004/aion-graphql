package org.satran.blockchain.graphql.model;

public class Output {

    public final static String HEX_TYPE = "HEX";
    public final static String BASE64_TYPE = "BASE64";

    private SolidityType type;
    private String encoding;
//    private Boolean array;

    public SolidityType getType() {
        return type;
    }

    public void setType(SolidityType type) {
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
