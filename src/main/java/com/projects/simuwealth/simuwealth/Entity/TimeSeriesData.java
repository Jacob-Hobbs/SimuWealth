package com.projects.simuwealth.simuwealth.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TimeSeriesData {

    @JsonProperty("1. open")
    private Double open;

    @JsonProperty("2. high")
    private Double high;

    @JsonProperty("3. low")
    private Double low;

    @JsonProperty("4. close")
    private Double close;

    @JsonProperty("5. volume")
    private Double volume;

    public TimeSeriesData() {
    }
}
