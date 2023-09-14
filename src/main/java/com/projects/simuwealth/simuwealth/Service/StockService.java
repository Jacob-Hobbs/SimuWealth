package com.projects.simuwealth.simuwealth.Service;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.simuwealth.simuwealth.Entity.AlphaVantageResponse;
import com.projects.simuwealth.simuwealth.Entity.TimeSeriesData;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class StockService {

    private final String alphaVantageApiKey = "EQY5LA8D04AV8HAN";
    private final String alphaVantageBaseUrl = "https://www.alphavantage.co/query";

    private final ObjectMapper objectMapper;

    public StockService() {
        this.objectMapper = new ObjectMapper();
    }

    public AlphaVantageResponse deserializeJson(String jsonData) {
        try {
            AlphaVantageResponse response = objectMapper.readValue(jsonData, AlphaVantageResponse.class);

            // Add debug prints to check if the deserialization worked
            System.out.println("Deserialized AlphaVantageResponse:");
            System.out.println(response);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Double getStockPrice(String ticker) {
        String apiUrl = alphaVantageBaseUrl +
                "?function=TIME_SERIES_INTRADAY&symbol=" + ticker +
                "&interval=1min&apikey=" + alphaVantageApiKey;

        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

        System.out.println("RESPONSE:");
        System.out.println("JSON Response: " + jsonResponse);

        AlphaVantageResponse response = deserializeJson(jsonResponse);

        try {
            if (response != null && response.getTimeSeries() != null) {
                Map<String, TimeSeriesData> timeSeries = response.getTimeSeries();

                // Debug prints to check the contents of timeSeries
                System.out.println("timeSeries: " + timeSeries);

                // Assuming you want to retrieve the latest available data point
                // Get the first entry in the timeSeries map (latest timestamp)
                String latestTimestamp = timeSeries.keySet().iterator().next();
                TimeSeriesData latestData = timeSeries.get(latestTimestamp);

                // Access the close price from the latest data
                Double latestClosePrice = latestData.getClose();
                System.out.println("latestClosePrice: " + latestClosePrice);

                return latestClosePrice;
            } else {
                // Handle the case where no time series data is available
                System.out.println("No time series data available.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception or log it for further investigation
            return null;
        }
    }
}
