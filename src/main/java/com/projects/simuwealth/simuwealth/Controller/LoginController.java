package com.projects.simuwealth.simuwealth.Controller;

import com.projects.simuwealth.simuwealth.Entity.User;
import com.projects.simuwealth.simuwealth.Repository.UserRepository;
import com.projects.simuwealth.simuwealth.Service.MailService.EmailSenderService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @PostConstruct
    public void loadSampleData() {
        User defaultAdmin = new User();
        defaultAdmin.setEmail("admin@simuwealth.com");
        defaultAdmin.setFirstName("Jacob");
        defaultAdmin.setLastName("Hobbs");
        defaultAdmin.setPassword("password");

        User testAdmin = new User();
        testAdmin.setEmail("jrh0397@gmail.com");
        testAdmin.setFirstName("Jacob");
        testAdmin.setLastName("Hobbs");
        testAdmin.setPassword("password");

        User jackiAdmin = new User();
        jackiAdmin.setEmail("jackimcgoon@gmail.com");
        jackiAdmin.setFirstName("Jacki");
        jackiAdmin.setLastName("Hobbs");
        jackiAdmin.setPassword("password");

        userRepository.save(defaultAdmin);
        userRepository.save(testAdmin);
        userRepository.save(jackiAdmin);
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

    @GetMapping("/resetPassword")
    public String resetPassword(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("error", false);
        return "resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("email") String email, Model model) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            model.addAttribute("error", true);
            return "resetPassword";
        } else {

            String subject = "Password Reset";
            String body = "Please follow the following link to reset your email: http://localhost:8080/resetPassword";

            emailSenderService.sendEmail(email, subject, body);

        }
        return "redirect:/login";


    }



}
