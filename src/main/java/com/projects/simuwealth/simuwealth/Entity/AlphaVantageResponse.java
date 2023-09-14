package com.projects.simuwealth.simuwealth.Entity;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlphaVantageResponse {

    @JsonProperty("Time Series (1min)")
    private Map<String, TimeSeriesData> timeSeries;

}
