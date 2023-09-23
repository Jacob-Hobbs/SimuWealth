package com.projects.simuwealth.simuwealth.Service.ApiService;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlphaVantageResponse {

    @JsonProperty("Time Series (5min)")
    private Map<String, TimeSeriesData> timeSeries;

    private Map<String, StockData> stockDataMap = new HashMap<>();

    @JsonAnySetter
    public void addStockData(String key, StockData value) {
        stockDataMap.put(key, value);
    }

    @JsonProperty("Meta Data")
    private MetaData metaData;

}
