package com.projects.simuwealth.simuwealth.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.simuwealth.simuwealth.Entity.Stock;
import com.projects.simuwealth.simuwealth.Entity.User;
import com.projects.simuwealth.simuwealth.Repository.StockRepository;
import com.projects.simuwealth.simuwealth.Repository.UserRepository;
import com.projects.simuwealth.simuwealth.Service.ApiService.AlphaVantageResponse;
import com.projects.simuwealth.simuwealth.Service.ApiService.StockData;
import com.projects.simuwealth.simuwealth.Service.ApiService.TimeSeriesData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StockService {

    private final String alphaVantageApiKey = "CJ4959GSEKSYP5DW";
    private final String alphaVantageBaseUrl = "https://www.alphavantage.co/query";

    private final ObjectMapper objectMapper;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UserRepository userRepository;

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
                "&interval=5min&entitlement=delayed&apikey=" + alphaVantageApiKey;

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

    public StockData getGlobalQuote(String ticker) {
        String apiUrl = alphaVantageBaseUrl +
                "?function=GLOBAL_QUOTE&symbol=" + ticker +
                "&entitlement=delayed&apikey=" + alphaVantageApiKey;

        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

        System.out.println("JSON Response: " + jsonResponse);

        AlphaVantageResponse response = deserializeJson(jsonResponse);

        System.out.println("GLOBAL DESERIALIZED RESPONSE: " + response);

        try {
            if (response != null && response.getStockDataMap() != null) {
                // Use the correct key to retrieve the StockData object
                StockData stockData = response.getStockDataMap().get("Global Quote - DATA DELAYED BY 15 MINUTES");
                System.out.println("StockData: " + stockData); // Add this line for debugging
                return stockData;
            } else {
                // Handle the case where no global quote data is available
                System.out.println("No global quote data available.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception or log it for further investigation
            return null;
        }
    }

    public void saveStock(Stock purchasedStock) {
        stockRepository.save(purchasedStock);
    }

    public List<Stock> getStocksByUser(User currentUser) {
        return stockRepository.findByUser(currentUser);
    }

    public double getRealTimeStockPrice(String symbol) {

        StockData stockData = getGlobalQuote(symbol);
        return Double.valueOf(stockData.getPrice());
    }


    public void updateUserStocks(User currentUser, List<Stock> stockList) {
        // Create a list to store stock IDs
        List<Integer> stockIds = new ArrayList<>();

        // Add the IDs of the stocks in the stockList to the stockIds list
        for (Stock stock : stockList) {
            stockIds.add(stock.getStock_Id());
        }

        // Set the stockIds list as the new stockList for the currentUser
        currentUser.setStockList(stockIds);

        // Save the updated user entity
        userRepository.save(currentUser);
    }

    public void deleteStock(Stock stock) {
        stockRepository.delete(stock);
    }

    public Map<String, TimeSeriesData> getTimeSeriesData(String ticker) {
        String apiUrl = alphaVantageBaseUrl +
                "?function=TIME_SERIES_INTRADAY&symbol=" + ticker +
                "&interval=5min&apikey=" + alphaVantageApiKey;

        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

        System.out.println("JSON Response for Time Series Data: " + jsonResponse);

        AlphaVantageResponse response = deserializeJson(jsonResponse);

        try {
            if (response != null && response.getTimeSeries() != null) {
                // Return the time series data
                return response.getTimeSeries();
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
