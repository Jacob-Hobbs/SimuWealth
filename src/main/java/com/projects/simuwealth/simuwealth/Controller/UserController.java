package com.projects.simuwealth.simuwealth.Controller;

import com.projects.simuwealth.simuwealth.CustomAnnotations.EmailExists;
import com.projects.simuwealth.simuwealth.Entity.User;
import com.projects.simuwealth.simuwealth.Repository.UserRepository;
import com.projects.simuwealth.simuwealth.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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
    public String userProfile(Model model, HttpServletRequest request) {

        User currentUser = (User) request.getSession().getAttribute("currentUser");

        System.out.println("Profile currentUser:");
        System.out.println(currentUser.getEmail());
        System.out.println(currentUser.getPassword());
        System.out.println(currentUser.getFirstName());
        System.out.println(currentUser.getLastName());

        model.addAttribute("currentUser", currentUser);

        return "profile";
    }

    @PostMapping("/userProfile")
    public String updateUserProfile(@ModelAttribute("currentUser") User updatedUser, Model model, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        // Handle user information updates (first name, last name, email)
        currentUser.setFirstName(updatedUser.getFirstName());
        currentUser.setLastName(updatedUser.getLastName());
        currentUser.setEmail(updatedUser.getEmail());

        System.out.println("SELECTED IMAGE: " + updatedUser.getProfilePicture());
        currentUser.setProfilePicture(updatedUser.getProfilePicture());


        // Save the updated user object to the database
        userRepository.save(currentUser);

        // Update the user in the current session
        request.getSession().setAttribute("currentUser", currentUser);

        return "redirect:/dashboard";
    }

    @GetMapping("/deleteAccount")
    public String deleteAccount(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        // Delete the user from the database
        userRepository.delete(currentUser);

        // Invalidate the session
        request.getSession().invalidate();

        // Redirect to the login page
        return "redirect:/login";
    }




}
