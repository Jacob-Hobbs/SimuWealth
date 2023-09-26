package com.projects.simuwealth.simuwealth.Controller;

import com.projects.simuwealth.simuwealth.Entity.Report;
import com.projects.simuwealth.simuwealth.Entity.Stock;
import com.projects.simuwealth.simuwealth.Entity.User;
import com.projects.simuwealth.simuwealth.Service.ApiService.StockData;
import com.projects.simuwealth.simuwealth.Service.ReportService;
import com.projects.simuwealth.simuwealth.Service.StockService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class ReportController {

    @Autowired
    private StockService stockService;

    @Autowired
    private ReportService reportService;

    @GetMapping("/reports")
    public String getWatchlist(Model model, HttpServletRequest request) {

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
            int userId = currentUser.getUserId();

            List<Report> allReports = reportService.getAllReports();

            // Filter reports by user ID
            List<Report> reportList = allReports.stream()
                    .filter(report -> report.getUser().getUserId() == userId)
                    .collect(Collectors.toList());

            // Convert the list of reports into a HashMap with reportId as the key
            Map<Integer, Report> reportMap = reportList.stream()
                    .collect(Collectors.toMap(report -> report.getReport_Id(), report -> report));

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("reportMap", reportMap);

            return "reports";
        } else {
            return "redirect:/login";
        }

    }

    @GetMapping("/generateReport")
    public String generateReport(Model model, HttpServletRequest request) {

        User currentUser = (User) request.getSession().getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);

        // Fetch necessary data from the currentUser
        int userId = currentUser.getUserId();
        // You can fetch other data related to the report from the currentUser

        // Create a new Report entity and set its properties
        Report report = new Report();
        report.setUser(currentUser);

        LocalDateTime reportDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' HH:mm:ss");
        String formattedDate = reportDate.format(formatter);

        report.setReportDate(formattedDate); // Set the report date

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

        report.setPercentReturn(String.valueOf(roundedPercentReturn));
        report.setTotalReturn(roundedTotalReturn);
        report.setTotalFunds(currentUser.getCapitol());


        // Save the report to the database using your service
        reportService.saveReport(report);

        // Redirect to the reports page or another appropriate page
        return "redirect:/reports";
    }

    @PostMapping("/deleteReport")
    public String removeReport(@RequestParam("reportId") int reportId, HttpServletRequest request) {
        // Get the currently logged-in user from the session
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        if (currentUser != null) {
            // Call a service method to delete the report from the database by reportId
            reportService.deleteReportById(reportId);

            // Redirect the user to the dashboard
            return "redirect:/dashboard";
        } else {
            // Handle the case when the user is not logged in (redirect to login page, for example)
            return "redirect:/login";
        }
    }


}
