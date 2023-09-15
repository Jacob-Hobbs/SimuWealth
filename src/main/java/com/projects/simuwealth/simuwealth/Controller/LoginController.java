package com.projects.simuwealth.simuwealth.Controller;

import com.projects.simuwealth.simuwealth.Entity.User;
import com.projects.simuwealth.simuwealth.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void loadSampleData() {
        User defaultAdmin = new User();
        defaultAdmin.setEmail("admin@simuwealth.com");
        defaultAdmin.setFirstName("Jacob");
        defaultAdmin.setLastName("Hobbs");
        defaultAdmin.setPassword("password");

        userRepository.save(defaultAdmin);
    }

    @GetMapping("/login")
    public String login(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "login";

    }

    @GetMapping("/forgotPassword")
    public String forgotPassword(Model model) {
        User user = new User();
        model.addAttribute("user", user);
                return "forgotPassword";
    }



}
