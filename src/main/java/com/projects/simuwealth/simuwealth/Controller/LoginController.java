package com.projects.simuwealth.simuwealth.Controller;

import com.projects.simuwealth.simuwealth.CustomAnnotations.EmailExists;
import com.projects.simuwealth.simuwealth.Entity.Stock;
import com.projects.simuwealth.simuwealth.Entity.User;
import com.projects.simuwealth.simuwealth.Repository.StockRepository;
import com.projects.simuwealth.simuwealth.Repository.UserRepository;
import com.projects.simuwealth.simuwealth.Service.MailService.EmailSenderService;
import com.projects.simuwealth.simuwealth.Service.StockService;
import com.projects.simuwealth.simuwealth.Service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    @PostConstruct
    public void loadSampleData() {

        // Delete all existing records to start with a clean slate
        stockRepository.deleteAll();
        userRepository.deleteAll();

        User defaultAdmin = userRepository.findById(String.valueOf(1)).orElseGet(() -> {
            User newUser = new User();
            newUser.setUserId(1);
            newUser.setEmail("admin@simuwealth.com");
            newUser.setFirstName("John");
            newUser.setLastName("Smith");
            newUser.setPassword("password");
            newUser.setCapitol(10999.99);

            return userRepository.save(newUser);
        });
        User testAdmin = userRepository.findById(String.valueOf(2)).orElseGet(() -> {
            User newUser = new User();
            newUser.setUserId(2);
            newUser.setEmail("jrh0397@gmail.com");
            newUser.setFirstName("Jacob");
            newUser.setLastName("Hobbs");
            newUser.setPassword("password");
            newUser.setCapitol(5450.39);
            return userRepository.save(newUser);
        });
        User jackiAdmin = userRepository.findById(String.valueOf(3)).orElseGet(() -> {
            User newUser = new User();
            newUser.setUserId(3);
            newUser.setEmail("jackimcgoon@gmail.com");
            newUser.setFirstName("Jacki");
            newUser.setLastName("Hobbs");
            newUser.setPassword("password");
            newUser.setCapitol(7333.22);
            return userRepository.save(newUser);
        });

        // Test Admin Stock:
        Stock stock1 = new Stock();
        stock1.setUser(testAdmin);
        stock1.setSymbol("F");
        stock1.setPurchasePrice(12.50);
        stockRepository.save(stock1);

        Stock stock2 = new Stock();
        stock2.setUser(testAdmin);
        stock2.setSymbol("F");
        stock2.setPurchasePrice(12.52);
        stockRepository.save(stock2);

        Stock stock3 = new Stock();
        stock3.setUser(testAdmin);
        stock3.setSymbol("KOS");
        stock3.setPurchasePrice(7.12);
        stockRepository.save(stock3);

        Stock stock4 = new Stock();
        stock4.setUser(testAdmin);
        stock4.setSymbol("KOS");
        stock4.setPurchasePrice(7.35);
        stockRepository.save(stock4);

        Stock stock5 = new Stock();
        stock5.setUser(testAdmin);
        stock5.setSymbol("KOS");
        stock5.setPurchasePrice(6.98);
        stockRepository.save(stock5);

        Stock stock11 = new Stock();
        stock11.setUser(testAdmin);
        stock11.setSymbol("MA");
        stock11.setPurchasePrice(405.44);
        stockRepository.save(stock11);

        Stock stock12 = new Stock();
        stock12.setUser(testAdmin);
        stock12.setSymbol("MA");
        stock12.setPurchasePrice(407.44);
        stockRepository.save(stock12);

        // Default Admin Stock:
        Stock stock6 = new Stock();
        stock6.setUser(defaultAdmin);
        stock6.setSymbol("MRO");
        stock6.setPurchasePrice(26.46);
        stockRepository.save(stock6);

        Stock stock7 = new Stock();
        stock7.setUser(defaultAdmin);
        stock7.setSymbol("MRO");
        stock7.setPurchasePrice(26.46);
        stockRepository.save(stock7);

        Stock stock8 = new Stock();
        stock8.setUser(defaultAdmin);
        stock8.setSymbol("MRO");
        stock8.setPurchasePrice(26.46);
        stockRepository.save(stock8);

        Stock stock9 = new Stock();
        stock9.setUser(defaultAdmin);
        stock9.setSymbol("AMZN");
        stock9.setPurchasePrice(139.06);
        stockRepository.save(stock9);

        Stock stock10 = new Stock();
        stock10.setUser(defaultAdmin);
        stock10.setSymbol("AMZN");
        stock10.setPurchasePrice(139.06);
        stockRepository.save(stock10);


    }

    @GetMapping("/login")
    public String login(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "login";

    }

    @PostMapping("/dashboard")
    public String loginUser(@ModelAttribute("user") User user, Model model, HttpServletRequest request, HttpSession session) {

        System.out.println("Email: " + user.getEmail());
        System.out.println("Password: " + user.getPassword());

        String userEmail = user.getEmail();
        User userdata = userRepository.findByEmail(userEmail);

        System.out.println("First Name: " + userdata.getFirstName());
        System.out.println("Last Name: " + userdata.getLastName());

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

        try {

            if ((user.getPassword().equals(userdata.getPassword())) && (user.getEmail().equals(userdata.getEmail()))) {

                // Check if the user has a profile picture; if not, set it to the default image
                if (userdata.getProfilePicture() == null) {

                    String defaultImagePath = "profilePicOne.png";
                    userdata.setProfilePicture(defaultImagePath);
                    userRepository.save(userdata);

                }

                // Store user in current session
                request.getSession().setAttribute("currentUser", userdata);
                session.setAttribute("currentUser", userdata);
                model.addAttribute("currentUser", userdata);

                System.out.println("Session info: ");
                System.out.println(session.getAttribute("currentUser").toString());


                return "dashboard";
            } else if ((user.getPassword().equals(userdata.getPassword())) && (!(user.getEmail().equals(userdata.getEmail())))) {
                model.addAttribute("invalidEmail", true);
                return "login";
            } else if ((!(user.getPassword().equals(userdata.getPassword()))) && (user.getEmail().equals(userdata.getEmail()))) {
                model.addAttribute("invalidPassword", true);
                return "login";
            }

        } catch (Exception e) {
            System.out.println("Incorrect username or password");
            model.addAttribute("invalidCredentials", true);
            return "login";
        }
        return "404";
    }










    @GetMapping("/forgotPassword")
    public String forgotPassword(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("emailNotFound", false);
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam("email") @Valid @EmailExists String email, Model model) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            System.out.println("BAD EMAIL!!");
            model.addAttribute("emailNotFound", true);
            model.addAttribute("user", new User());
            return "forgotPassword";
        } else {
            model.addAttribute("user", user);
            String subject = "Password Reset";
            String body = "Please follow the following link to reset your email: http://localhost:8080/resetPassword?email=" + email;

            emailSenderService.sendEmail(email, subject, body);

            return "redirect:/login";

        }
    }

    @GetMapping("/resetPassword")
    public String resetPassword(@RequestParam("email") String email, Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("email", email);
        return "resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("email") String email,
                                @RequestParam("passwordResetInput") String newPassword,
                                @RequestParam("passwordResetConfirmInput") String confirmPassword,
                                Model model) {

        System.out.println("Email: " + email);
        System.out.println("New Password: " + newPassword);
        System.out.println("Confirm Password: " + confirmPassword);
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("passwordMismatch", true);
            model.addAttribute("email", email);
            return "resetPassword";
        } else {
            userService.updatePasswordByEmail(email, newPassword);
            return "redirect:/login";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {

        // clears out all session data, including "currentUser" attribute
        request.getSession().invalidate();

        return "redirect:/login";
    }





}
