package com.projects.simuwealth.simuwealth.Controller;

import com.projects.simuwealth.simuwealth.CustomAnnotations.EmailExists;
import com.projects.simuwealth.simuwealth.Entity.User;
import com.projects.simuwealth.simuwealth.Service.UserService;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/registerUser")
    public String registrationPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/registerUser")
    public String registerUser(@ModelAttribute("user") User user,
                               @RequestParam("confirmPasswordInput") String confirmPassword,
                               Model model) {

        System.out.println("First name: " + user.getFirstName());
        System.out.println("Last name: " + user.getLastName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Password: " + user.getPassword());
        System.out.println("Confirm password: " + confirmPassword);

        if (userService.isEmailAlreadyInUse(user.getEmail())) {
            model.addAttribute("emailInUse", true);
            return "register";
        }

        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("passwordMismatch", true);
            return "register";
        } else {
            userService.registerUser(user);
            return "redirect:/login";
        }
    }

    @GetMapping("/userProfile")
    public String userProfile() {
        return "profile";
    }


}
