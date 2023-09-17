package com.projects.simuwealth.simuwealth.Controller;

import com.projects.simuwealth.simuwealth.Entity.User;
import com.projects.simuwealth.simuwealth.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DashboardController {

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {



        return "dashboard";
    }
}
