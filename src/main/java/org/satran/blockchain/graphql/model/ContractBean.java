package org.satran.blockchain.graphql.model;

import java.util.List;

public class ContractBean {

    private String contractName;
    private String contractAddress;

    private String from;
    private String deployTxId;

    private String encodedData;

    private List<ContractAbiEntryBean> abiDefinition;

    private String abiDefToString;

    private String source;
    private String code;
    private String compilerOptions;
    private String compilerVersion;
    private String developerDoc;
    private String languageVersion;
    private String userDoc;

//    ContractAbiEntry getAbiFunction();


    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDeployTxId() {
        return deployTxId;
    }

    public void setDeployTxId(String deployTxId) {
        this.deployTxId = deployTxId;
    }

    public String getEncodedData() {
        return encodedData;
    }

    public void setEncodedData(String encodedData) {
        this.encodedData = encodedData;
    }

    public String getAbiDefToString() {
        return abiDefToString;
    }

    public void setAbiDefToString(String abiDefToString) {
        this.abiDefToString = abiDefToString;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompilerOptions() {
        return compilerOptions;
    }

    public void setCompilerOptions(String compilerOptions) {
        this.compilerOptions = compilerOptions;
    }

    public String getCompilerVersion() {
        return compilerVersion;
    }

    public void setCompilerVersion(String compilerVersion) {
        this.compilerVersion = compilerVersion;
    }

    public String getDeveloperDoc() {
        return developerDoc;
    }

    public void setDeveloperDoc(String developerDoc) {
        this.developerDoc = developerDoc;
    }

    public String getLanguageVersion() {
        return languageVersion;
    }

    public void setLanguageVersion(String languageVersion) {
        this.languageVersion = languageVersion;
    }

    public String getUserDoc() {
        return userDoc;
    }

    public void setUserDoc(String userDoc) {
        this.userDoc = userDoc;
    }

    public List<ContractAbiEntryBean> getAbiDefinition() {
        return abiDefinition;
    }

    public void setAbiDefinition(List<ContractAbiEntryBean> abiDefinition) {
        this.abiDefinition = abiDefinition;
    }
}

