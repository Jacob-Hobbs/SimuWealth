package com.projects.simuwealth.simuwealth.Entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaData {

    @JsonProperty("1. Information")
    @JsonAlias("Information")
    private String information;

    @JsonProperty("2. Symbol")
    @JsonAlias("Symbol")
    private String symbol;

    public MetaData(String information, String symbol) {
        this.information = information;
        this.symbol = symbol;
    }

    public MetaData() {
        // Blank constructor as required by JPA (if needed)
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "MetaData{" +
                "information='" + information + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}