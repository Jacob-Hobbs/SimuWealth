package com.projects.simuwealth.simuwealth.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.simuwealth.simuwealth.Entity.Stock;
import com.projects.simuwealth.simuwealth.Entity.User;
import com.projects.simuwealth.simuwealth.Service.ApiService.StockData;
import com.projects.simuwealth.simuwealth.Service.ApiService.TimeSeriesData;
import com.projects.simuwealth.simuwealth.Service.StockService;
import com.projects.simuwealth.simuwealth.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/{symbol}/price")
    public Double getStockPrice(@PathVariable String symbol) {
        return stockService.getStockPrice(symbol);
    }

    @GetMapping("/latest-stock-price")
    public String getLatestStockPrice(Model model) {
        try {
            Double latestStockPrice = stockService.getStockPrice("F");
            System.out.println("Ford price: " + latestStockPrice);
            if (latestStockPrice != null) {
                model.addAttribute("latestStockPrice", latestStockPrice);
                return "index"; // Return the HTML page with the stock price.
            } else {
                throw new Exception("Unable to retrieve the latest stock price for Ford.");
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "404"; // Create an error Thymeleaf template for displaying errors.
        }
    }

    @GetMapping("/buyStock")
    public String buyStockPage(Model model, @RequestParam String stockSymbol, HttpServletRequest request) {

        User currentUser = (User) request.getSession().getAttribute("currentUser");

        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);

            // Fetch global quote data for the specified stockSymbol
            StockData stockData = stockService.getGlobalQuote(stockSymbol);

            if (stockData != null) {
                // Check if the stockData contains valid data
                if (stockData.getOpen() != null && stockData.getHigh() != null && stockData.getLow() != null &&
                        stockData.getVolume() != null && stockData.getPreviousClose() != null) {
                    model.addAttribute("stockData", stockData);
                    return "buyStock";
                } else {
                    // Handle the case where the stockData has missing or null values
                    return "404";
                }
            } else {
                // Handle the case where no data is available for the given stockSymbol
                return "404";
            }
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/sellStock")
    public String sellStockPage(Model model, @RequestParam String stockSymbol, @RequestParam double shares, HttpServletRequest request) {

        User currentUser = (User) request.getSession().getAttribute("currentUser");

        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);

            // Fetch global quote data for the specified stockSymbol
            StockData stockData = stockService.getGlobalQuote(stockSymbol);

            if (stockData != null) {
                // Check if the stockData contains valid data
                if (stockData.getOpen() != null && stockData.getHigh() != null && stockData.getLow() != null &&
                        stockData.getVolume() != null && stockData.getPreviousClose() != null) {
                    System.out.println("NUMBER OF SHARES: " + shares);
                    model.addAttribute("stockData", stockData);
                    model.addAttribute("shares", shares);
                    return "sellStock";
                } else {
                    // Handle the case where the stockData has missing or null values
                    return "404";
                }
            } else {
                // Handle the case where no data is available for the given stockSymbol
                return "404";
            }
        } else {
            return "redirect:/login";
        }
    }


    @PostMapping("/sellStock")
    public String sellStock(
            @RequestParam String stockSymbol,
            @RequestParam Double currentPrice,
            @RequestParam Integer purchaseQuantity,
            HttpServletRequest request,
            Model model) {

        // Retrieve the currentUser from the session
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        // Check if currentUser is not null
        if (currentUser != null) {
            // Get the user's stock list
            List<Stock> stockList = stockService.getStocksByUser(currentUser);

            // Calculate the purchase amount
            double saleAmount = currentPrice * purchaseQuantity;

            // Check if currentUser has enough shares to sell
            long sharesOwned = stockList.stream()
                    .filter(stock -> stock.getSymbol().equals(stockSymbol))
                    .count();

            if (sharesOwned >= purchaseQuantity) {
                // Convert user's capital to BigDecimal for precision
                BigDecimal userCapital = new BigDecimal(currentUser.getCapitol()).setScale(2, BigDecimal.ROUND_HALF_UP);

                // Subtract the purchase amount from the currentUser's capital
                BigDecimal newCapital = userCapital.add(new BigDecimal(saleAmount)).setScale(2, BigDecimal.ROUND_HALF_UP);

                // Update the currentUser's capital in the database
                currentUser.setCapitol(newCapital.doubleValue());

                // Remove the sold shares from the database
                List<Stock> soldStocks = stockList.stream()
                        .filter(stock -> stock.getSymbol().equals(stockSymbol))
                        .limit(purchaseQuantity) // Limit to the number of shares being sold
                        .collect(Collectors.toList());

                for (Stock soldStock : soldStocks) {
                    stockService.deleteStock(soldStock); // Add this method in StockService
                }

                // Redirect to a success page or perform any other necessary actions
                return "redirect:/dashboard";
            } else {
                // Handle the case where currentUser does not have enough shares to sell
                model.addAttribute("error", "Not enough shares to sell");
                return "404"; // You can define an error page for this case
            }
        } else {
            // Handle the case where currentUser is null (user not logged in)
            return "redirect:/login";
        }
    }


    @PostMapping("/buyStock")
    public String purchaseStock(
            @RequestParam String stockSymbol,
            @RequestParam Double currentPrice,
            @RequestParam Integer purchaseQuantity,
            HttpServletRequest request,
            Model model) {

        // Retrieve the currentUser from the session
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        // Calculate the purchase amount
        double purchaseAmount = currentPrice * purchaseQuantity;


        // Check if currentUser is not null
        if (currentUser != null) {
            // Convert user's capital to BigDecimal for precision
            BigDecimal userCapital = new BigDecimal(currentUser.getCapitol()).setScale(2, BigDecimal.ROUND_HALF_UP);

            // Check if currentUser has enough capital for the purchase
            BigDecimal purchaseTotal = new BigDecimal(purchaseAmount).setScale(2, BigDecimal.ROUND_HALF_UP);

            if (userCapital.compareTo(purchaseTotal) >= 0) {
                // Subtract the purchase amount from the currentUser's capital
                BigDecimal newCapital = userCapital.subtract(purchaseTotal);

                // Update the currentUser's capital in the database
                currentUser.setCapitol(newCapital.doubleValue());

                System.out.println("Purchase Quantity: " + purchaseQuantity);
                System.out.println("Current User: " + currentUser);
                System.out.println("Symbol: " + stockSymbol);
                System.out.println("Current Price: " + currentPrice);

                // Add the purchased stock to the user's stockList
                for (int i = 0; i < purchaseQuantity; i++) {
                    Stock purchasedStock = new Stock();
                    purchasedStock.setUser(currentUser);
                    purchasedStock.setSymbol(stockSymbol);
                    purchasedStock.setPurchasePrice(currentPrice);
                    // Save the purchased stock to the database
                    stockService.saveStock(purchasedStock);
                }

                // Update the currentUser in the database
                userService.updateUser(currentUser);

                // Redirect to a success page or perform any other necessary actions
                return "redirect:/dashboard";
            } else {
                // use case for invalid transaction
                return "buyStock";
            }
        } else {
            // Handle the case where currentUser is null (user not logged in)
            return "redirect:/login";
        }
    }






















    @GetMapping("/stockDetails")
    public String stockDetailsPage(Model model, @RequestParam String stockSymbol, HttpServletRequest request) throws JsonProcessingException {
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        List<Double> marqueePrices = new ArrayList<>();
        marqueePrices.add(Math.round(stockService.getRealTimeStockPrice("F") * 100.0) / 100.0);
        marqueePrices.add(Math.round(stockService.getRealTimeStockPrice("TSLA") * 100.0) / 100.0);
        marqueePrices.add(Math.round(stockService.getRealTimeStockPrice("AAPL") * 100.0) / 100.0);
        marqueePrices.add(Math.round(stockService.getRealTimeStockPrice("AMZN") * 100.0) / 100.0);
        marqueePrices.add(Math.round(stockService.getRealTimeStockPrice("NVDA") * 100.0) / 100.0);
        marqueePrices.add(Math.round(stockService.getRealTimeStockPrice("BAC") * 100.0) / 100.0);
        marqueePrices.add(Math.round(stockService.getRealTimeStockPrice("MMP") * 100.0) / 100.0);
        marqueePrices.add(Math.round(stockService.getRealTimeStockPrice("INTC") * 100.0) / 100.0);
        marqueePrices.add(Math.round(stockService.getRealTimeStockPrice("MSFT") * 100.0) / 100.0);
        model.addAttribute("marqueePrices", marqueePrices);
        List<String> marqueePercents = new ArrayList<>();
        marqueePercents.add(stockService.getGlobalQuote("F").getChangePercent());
        marqueePercents.add(stockService.getGlobalQuote("TSLA").getChangePercent());
        marqueePercents.add(stockService.getGlobalQuote("AAPL").getChangePercent());
        marqueePercents.add(stockService.getGlobalQuote("AMZN").getChangePercent());
        marqueePercents.add(stockService.getGlobalQuote("NVDA").getChangePercent());
        marqueePercents.add(stockService.getGlobalQuote("BAC").getChangePercent());
        marqueePercents.add(stockService.getGlobalQuote("MMP").getChangePercent());
        marqueePercents.add(stockService.getGlobalQuote("INTC").getChangePercent());
        marqueePercents.add(stockService.getGlobalQuote("MSFT").getChangePercent());
        model.addAttribute("marqueePercents", marqueePercents);

        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);

            // Fetch global quote data for the specified stockSymbol
            StockData stockData = stockService.getGlobalQuote(stockSymbol);

            if (stockData != null) {
                // Check if the stockData contains valid data
                if (stockData.getOpen() != null && stockData.getHigh() != null && stockData.getLow() != null &&
                        stockData.getVolume() != null && stockData.getPreviousClose() != null) {
                    model.addAttribute("stockData", stockData);

                    List<Double> reverseClosePrices = null;
                    List<Integer> minutes = null;
                    try {
                        // Fetch and include the time series data in the model
                        Map<String, TimeSeriesData> timeSeriesData = stockService.getTimeSeriesData(stockSymbol);

                        List<Double> closePrices = new ArrayList<>();
                        reverseClosePrices = new ArrayList<>();
                        List<String> timeIntervals = new ArrayList<>();
                        minutes = new ArrayList<>();

                        for (String timestamp : timeSeriesData.keySet()) {
                            TimeSeriesData dataPoint = timeSeriesData.get(timestamp);
                            if (dataPoint != null) {
                                closePrices.add(dataPoint.getClose());
                                timeIntervals.add(timestamp);
                            }
                        }
                        minutes.add(0);
                        for (int i = 1; i < 100; i++) {
                            minutes.add(i * 5);
                        }

                        for (int i = 99; i >= 0; i--) {
                            double num = closePrices.get(i);
                            reverseClosePrices.add(num);
                        }


                        System.out.println("Close Prices: ");
                        for (Double closePrice : closePrices) {
                            System.out.println(closePrice);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }


                    // Add these lists as attributes to your model
                    model.addAttribute("closePrices", reverseClosePrices);

                    model.addAttribute("minutesList", minutes);

                    return "stockDetails";
                } else {
                    // Handle the case where the stockData has missing or null values
                    return "404";
                }
            } else {
                // Handle the case where no data is available for the given stockSymbol
                return "404";
            }
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/stockDetails")
    public String buyStockPage(Model model, @RequestParam String stockSymbol, @RequestParam Double currentPrice, HttpServletRequest request) {
        // Retrieve the currentUser and other data as needed
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        request.getSession().setAttribute("currentUser", currentUser);
        model.addAttribute("currentUser", currentUser);

        // Populate the model with data for the /buyStock page
        model.addAttribute("stockSymbol", stockSymbol);
        model.addAttribute("currentPrice", currentPrice);


        // Add other data to the model as needed

        return "buyStock"; // Return the /buyStock page
    }

    @PostMapping("/portfolioSell")
    public String sellStockPageFromPortfolio(Model model, @RequestParam String stockSymbol, @RequestParam Double currentPrice, @RequestParam Double shares, HttpServletRequest request) {
        // Retrieve the currentUser and other data as needed
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        request.getSession().setAttribute("currentUser", currentUser);
        model.addAttribute("currentUser", currentUser);

        // Populate the model with data for the /buyStock page
        model.addAttribute("stockSymbol", stockSymbol);
        model.addAttribute("currentPrice", currentPrice);
        model.addAttribute("shares", shares);

        // Add other data to the model as needed

        return "sellStock"; // Return the /buyStock page
    }




    @PostMapping("/addToWatchlist")
    public String addToWatchlist(Model model, @RequestParam String stockSymbol, HttpServletRequest request) {
        // Retrieve the currentUser and other data as needed
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        List<String> watchlist = currentUser.getWatchlist();

        System.out.println("WATCHLIST CONTENTS: " + watchlist.toString());

        if (!watchlist.contains(stockSymbol)) {
            watchlist.add(stockSymbol);

            System.out.println("WATCHLIST CONTENTS AFTER ADD: " + watchlist.toString());

            // Update the user's watchlist without saving the entire user object
            userService.addToWatchlist(currentUser, watchlist);

            System.out.println("WATCHLIST CONTENTS WHOLE: " + currentUser.getWatchlist().toString());

            return "redirect:/watchlist"; // Redirect to the watchlist page
        }

        System.out.println(stockSymbol + " already added to watchlist!!");

        return "redirect:/watchlist"; // Redirect to the watchlist page
    }




}
