package com.projects.simuwealth.simuwealth.Controller;

import com.projects.simuwealth.simuwealth.CustomAnnotations.EmailExists;
import com.projects.simuwealth.simuwealth.Entity.User;
import com.projects.simuwealth.simuwealth.Repository.UserRepository;
import com.projects.simuwealth.simuwealth.Service.MailService.EmailSenderService;
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

@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

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

    @PostMapping("/dashboard")
    public String loginUser(@ModelAttribute("user") User user, Model model, HttpServletRequest request, HttpSession session) {

        System.out.println("Email: " + user.getEmail());
        System.out.println("Password: " + user.getPassword());

        String userEmail = user.getEmail();
        User userdata = userRepository.findByEmail(userEmail);

        System.out.println("First Name: " + userdata.getFirstName());
        System.out.println("Last Name: " + userdata.getLastName());

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
