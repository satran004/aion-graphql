package org.satran.blockchain.graphql.model;

import java.math.BigInteger;

public class ContractDeployBean {

//    private CompileResponseBean compileResponse;
    private String code;
    private String data;
    private String from;
    private long nrgLimit;
    private long nrgPrice;
    private BigInteger value;
    private boolean isConstructor;

//    public CompileResponseBean getCompileResponse() {
//        return compileResponse;
//    }
//
//    public void setCompileResponse(CompileResponseBean compileResponse) {
//        this.compileResponse = compileResponse;
//    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getNrgLimit() {
        return nrgLimit;
    }

    public void setNrgLimit(long nrgLimit) {
        this.nrgLimit = nrgLimit;
    }

    public long getNrgPrice() {
        return nrgPrice;
    }

    public void setNrgPrice(long nrgPrice) {
        this.nrgPrice = nrgPrice;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public boolean isConstructor() {
        return isConstructor;
    }

    public void setConstructor(boolean constructor) {
        isConstructor = constructor;
    }

}
