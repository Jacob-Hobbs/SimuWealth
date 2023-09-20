package com.projects.simuwealth.simuwealth.Controller;

import com.projects.simuwealth.simuwealth.Entity.User;
import com.projects.simuwealth.simuwealth.Service.ApiService.StockData;
import com.projects.simuwealth.simuwealth.Service.StockService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class StockController {

    @Autowired
    private StockService stockService;

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
    public String buyStockPage(Model model, HttpServletRequest request) {

        User currentUser = (User) request.getSession().getAttribute("currentUser");


        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
            return "buyStock";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/stockDetails")
    public String stockDetailsPage(Model model, @RequestParam String stockSymbol, HttpServletRequest request) {

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




}
