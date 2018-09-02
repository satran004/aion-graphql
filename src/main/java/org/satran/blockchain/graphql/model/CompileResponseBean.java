package org.satran.blockchain.graphql.model;

import java.util.List;

public class CompileResponseBean {

    private List<ContractAbiEntry> abiDefinition;
    private String abiDefString;
    private String code;
    private String compilerOptions;
    private String compilerVersion;
//    private String developerDoc; //TODO
    private String language;
    private String languageVersion;
    private String source;
//    private String userDoc; //TODO


    public List<ContractAbiEntry> getAbiDefinition() {
        return abiDefinition;
    }

    public void setAbiDefinition(List<ContractAbiEntry> abiDefinition) {
        this.abiDefinition = abiDefinition;
    }

    public String getAbiDefString() {
        return abiDefString;
    }

    public void setAbiDefString(String abiDefString) {
        this.abiDefString = abiDefString;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguageVersion() {
        return languageVersion;
    }

    public void setLanguageVersion(String languageVersion) {
        this.languageVersion = languageVersion;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}

class ContractAbiEntry {

    private String hashed;
    private boolean constructor;
    private boolean event;
    private boolean fallback;

    public String getHashed() {
        return hashed;
    }

    public void setHashed(String hashed) {
        this.hashed = hashed;
    }

    public boolean isConstructor() {
        return constructor;
    }

    public void setConstructor(boolean constructor) {
        this.constructor = constructor;
    }

    public boolean isEvent() {
        return event;
    }

    public void setEvent(boolean event) {
        this.event = event;
    }

    public boolean isFallback() {
        return fallback;
    }

    public void setFallback(boolean fallback) {
        this.fallback = fallback;
    }
}

class ContractAbiIOParam {

    private String name;
    private List<Integer> paramLengths;
    private String type;
    private boolean indexed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getParamLengths() {
        return paramLengths;
    }

    public void setParamLengths(List<Integer> paramLengths) {
        this.paramLengths = paramLengths;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isIndexed() {
        return indexed;
    }

    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }
}
