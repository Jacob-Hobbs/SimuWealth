package com.projects.simuwealth.simuwealth.Service.ApiService;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class StockData {

    @JsonProperty("01. symbol")
    private String symbol;

    @JsonProperty("02. open")
    private String open;

    @JsonProperty("03. high")
    private String high;

    @JsonProperty("04. low")
    private String low;

    @JsonProperty("05. price")
    private String price;

    @JsonProperty("06. volume")
    private String volume;

    @JsonProperty("07. latest trading day")
    private String latestTradingDay;

    @JsonProperty("08. previous close")
    private String previousClose;

    @JsonProperty("09. change")
    private String change;

    @JsonProperty("10. change percent")
    private String changePercent;

    public StockData() {
    }

    // Add a constructor to initialize fields
    public StockData(String symbol, String open, String high, String low, String price, String volume,
                     String latestTradingDay, String previousClose, String change, String changePercent) {
        this.symbol = symbol;
        this.open = open;
        this.high = high;
        this.low = low;
        this.price = price;
        this.volume = volume;
        this.latestTradingDay = latestTradingDay;
        this.previousClose = previousClose;
        this.change = change;
        this.changePercent = changePercent;
    }

    @Override
    public String toString() {
        return "StockData{" +
                "symbol='" + symbol + '\'' +
                ", open='" + open + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", price='" + price + '\'' +
                ", volume='" + volume + '\'' +
                ", latestTradingDay='" + latestTradingDay + '\'' +
                ", previousClose='" + previousClose + '\'' +
                ", change='" + change + '\'' +
                ", changePercent='" + changePercent + '\'' +
                '}';
    }
}