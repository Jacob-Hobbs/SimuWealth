package com.projects.simuwealth.simuwealth.Controller;

import com.projects.simuwealth.simuwealth.Entity.User;
import com.projects.simuwealth.simuwealth.Service.ApiService.StockData;
import com.projects.simuwealth.simuwealth.Service.StockService;
import com.projects.simuwealth.simuwealth.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private UserService userService;

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

    @PostMapping("/buyStock")
    public String purchaseStock(@RequestParam String purchaseTotal, HttpServletRequest request) {
        // Retrieve the currentUser from the session
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        // Parse the purchaseTotal string to a BigDecimal with two decimal places
        BigDecimal purchaseAmount = new BigDecimal(purchaseTotal).setScale(2, BigDecimal.ROUND_HALF_UP);

        System.out.println("PURCHASE AMOUNT: " + purchaseAmount);

        // Check if currentUser is not null
        if (currentUser != null) {
            // Convert user's capital to BigDecimal for precision
            BigDecimal userCapital = new BigDecimal(currentUser.getCapitol()).setScale(2, BigDecimal.ROUND_HALF_UP);

            // Check if currentUser has enough capital for the purchase
            if (userCapital.compareTo(purchaseAmount) >= 0) {
                // Subtract the purchase amount from the currentUser's capital
                BigDecimal newCapital = userCapital.subtract(purchaseAmount);

                // Update the currentUser's capital in the database
                currentUser.setCapitol(newCapital.doubleValue()); // Convert back to double for database storage

                // Update the currentUser in the database (use your service/repository layer)
                userService.updateUser(currentUser);

                // Redirect to a success page or perform any other necessary actions
                return "redirect:/dashboard";
            } else {
                // Handle the case where the user doesn't have enough capital
                return "redirect:/buyStock";
            }
        } else {
            // Handle the case where currentUser is null (user not logged in)
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



}
