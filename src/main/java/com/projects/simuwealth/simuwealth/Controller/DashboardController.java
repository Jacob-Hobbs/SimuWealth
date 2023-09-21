package com.projects.simuwealth.simuwealth.Controller;

import com.projects.simuwealth.simuwealth.Entity.User;
import com.projects.simuwealth.simuwealth.Repository.UserRepository;
import com.projects.simuwealth.simuwealth.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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


        return "portfolio";
    }












}
