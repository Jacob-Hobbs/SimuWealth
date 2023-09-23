package com.projects.simuwealth.simuwealth.Controller;

import com.projects.simuwealth.simuwealth.Entity.Stock;
import com.projects.simuwealth.simuwealth.Entity.User;
import com.projects.simuwealth.simuwealth.Repository.UserRepository;
import com.projects.simuwealth.simuwealth.Service.ApiService.StockData;
import com.projects.simuwealth.simuwealth.Service.StockService;
import com.projects.simuwealth.simuwealth.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockService stockService;

    @GetMapping("/dashboard")
    public String getDashboard(Model model, HttpServletRequest request) {

        User currentUser = (User) request.getSession().getAttribute("currentUser");


        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
            return "dashboard";
        } else {
            return "redirect:/login";
        }

    }

    @GetMapping("/manageFunds")
    public String getManageFundsPage(Model model, HttpServletRequest request) {

        User currentUser = (User) request.getSession().getAttribute("currentUser");

        model.addAttribute("currentUser", currentUser);


        return "funds";
    }

    @PostMapping("/manageFunds")
    public String updateFunds(@ModelAttribute("currentUser") User updatedUser, Model model, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        // Update the capitol field
        currentUser.setCapitol(updatedUser.getCapitol());

        // Save the updated user object to the database
        userRepository.save(currentUser);

        // Update the user in the current session
        request.getSession().setAttribute("currentUser", currentUser);

        return "redirect:/dashboard";
    }

    @GetMapping("/portfolio")
    public String getPortfolio(Model model, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);

        // Retrieve the user's current capital from your data source
        double currentCapital = currentUser.getCapitol();

        // Retrieve the list of stocks the user holds
        List<Stock> userStocks = stockService.getStocksByUser(currentUser);

        // Create a map to store the quantity of each stock symbol
        Map<String, Integer> stockQuantities = new HashMap<>();
        Map<String, Double> stockMarketValues = new HashMap<>();
        Map<String, Double> shareAverageCost = new HashMap<>();
        Map<String, Double> dollarReturn = new HashMap<>();
        Map<String, Double> currentPrice = new HashMap<>();
        Map<String, Double> percentReturnMap = new HashMap<>();
        Map<String, StockData> stockData = new HashMap<>();

        // Calculate the total value of the user's stock holdings
        double totalStockValue = 0.0;

        // List that contains a unique stock symbols.
        List<String> stockSymbols = new ArrayList<>();

        for (Stock stock: userStocks) {
            // Increment the quantity for this stock symbol
            stockQuantities.put(stock.getSymbol(), stockQuantities.getOrDefault(stock.getSymbol(), 0) + 1);
            if (!stockSymbols.contains(stock.getSymbol())) {
                stockSymbols.add(stock.getSymbol());
            }
        }

        for (String symbol: stockSymbols) {

            double realTimePrice = stockService.getRealTimeStockPrice(symbol);
            double roundedRealtimePrice = Math.round(realTimePrice * 100.0) / 100.0;
            currentPrice.put(symbol, roundedRealtimePrice);



            totalStockValue = stockQuantities.get(symbol) * realTimePrice;

            double roundedTotalStockValue = Math.round(totalStockValue * 100.0) / 100.0;
            stockMarketValues.put(symbol, roundedTotalStockValue);


        }

        for (String symbol: stockSymbols) {

            int count = 0;
            double totalPurchaseAmount = 0.0;

            for (Stock stock: userStocks) {
                if (symbol.equals(stock.getSymbol())) {
                    totalPurchaseAmount += stock.getPurchasePrice();
                    count += 1;
                }

            }
            double avgPrice = totalPurchaseAmount / count;
            double roundedAveragePrice = Math.round(avgPrice * 100.0) / 100.0;
            shareAverageCost.put(symbol, roundedAveragePrice);

            double monetaryReturn = stockMarketValues.get(symbol) - totalPurchaseAmount;

            double roundedMonetaryReturn = Math.round(monetaryReturn * 100.0) / 100.0;
            dollarReturn.put(symbol, roundedMonetaryReturn);

            double percentReturnAmount = ((stockMarketValues.get(symbol) - totalPurchaseAmount) / totalPurchaseAmount) * 100;
            System.out.println("STOCK MARKET VALUE: " + symbol + ": " + stockMarketValues.get(symbol));
            System.out.println("TOTAL PURCHASE AMOUNT: " + symbol + ": " + totalPurchaseAmount);
            double roundedPercentReturnAmount = Math.round(percentReturnAmount * 100.0) / 100.0;
            percentReturnMap.put(symbol, roundedPercentReturnAmount);

        }

        Double totalCapital = currentCapital;

        for (String symbol: stockSymbols) {
            totalCapital += stockMarketValues.get(symbol);

            stockData.put(symbol, stockService.getGlobalQuote(symbol));

        }

        double roundedTotalCapital = Math.round(totalCapital * 100.0) / 100.0;

        // Add the portfolio value to the model
        model.addAttribute("portfolioValue", roundedTotalCapital);

        double stockTotalCost = 0.0;
        for (Stock stock: userStocks) {
            double purchasePrice = stock.getPurchasePrice();
            stockTotalCost += purchasePrice;
        }

        double baseInvestment = currentCapital + stockTotalCost;
        double totalReturn = roundedTotalCapital - baseInvestment;
        double roundedTotalReturn = Math.round(totalReturn * 100.0) / 100.0;
        double percentReturn = (totalReturn / baseInvestment) * 100;
        double roundedPercentReturn = Math.round(percentReturn * 100.0) / 100.0;

        model.addAttribute("totalReturn", roundedTotalReturn);
        model.addAttribute("percentReturn", roundedPercentReturn);
        model.addAttribute("stockSymbols", stockSymbols);
        model.addAttribute("stockQuantities", stockQuantities);
        model.addAttribute("stockMarketValues", stockMarketValues);
        model.addAttribute("shareAverageCost", shareAverageCost);
        model.addAttribute("dollarReturn", dollarReturn);
        model.addAttribute("currentPrice", currentPrice);
        model.addAttribute("percentReturnMap", percentReturnMap);
        model.addAttribute("stockData", stockData);

        return "portfolio";
    }

    @GetMapping("/data")
    public ResponseEntity<Map<String, Double>> getPortfolioData(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        List<Stock> userStocks = stockService.getStocksByUser(currentUser);

        Map<String, Double> stockTypePercentages = new HashMap<>();

        for (Stock stock : userStocks) {
            String symbol = stock.getSymbol();
            stockTypePercentages.put(symbol, stockTypePercentages.getOrDefault(symbol, 0.0) + 1.0);
        }

        // Calculate percentages
        double totalStocks = userStocks.size();
        for (String symbol : stockTypePercentages.keySet()) {
            double percentage = (stockTypePercentages.get(symbol) / totalStocks) * 100.0;
            stockTypePercentages.put(symbol, percentage);
        }

        return ResponseEntity.ok(stockTypePercentages);
    }

    @GetMapping("/money-data")
    public ResponseEntity<Map<String, Double>> getMoneyData(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        List<Stock> userStocks = stockService.getStocksByUser(currentUser);

        Map<String, Double> stockTypePercentages = new HashMap<>();
        Map<String, Double> moneySpentData = new HashMap<>();

        // Iterate through userStocks to calculate both datasets
        double totalStocks = 0.0;
        double totalMoneySpent = 0.0;

        for (Stock stock : userStocks) {
            String symbol = stock.getSymbol();

            // Calculate the total number of stocks for each symbol
            stockTypePercentages.put(symbol, stockTypePercentages.getOrDefault(symbol, 0.0) + 1.0);

            // Calculate the total money spent for each symbol
            double pricePerShare = stock.getPurchasePrice();
            double moneySpent = pricePerShare;
            moneySpentData.put(symbol, moneySpentData.getOrDefault(symbol, 0.0) + moneySpent);

            totalStocks += 1.0; // Increment total stocks
            totalMoneySpent += moneySpent; // Increment total money spent
        }

        // Calculate percentages for moneySpentData
        for (String symbol : moneySpentData.keySet()) {
            double percentage = (moneySpentData.get(symbol) / totalMoneySpent) * 100.0;
            moneySpentData.put(symbol, percentage);
        }

        return ResponseEntity.ok(moneySpentData);
    }

}
