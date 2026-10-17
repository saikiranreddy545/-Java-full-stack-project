package com.example.online.e_commerce.shopping.system.controller;

import com.example.online.e_commerce.shopping.system.model.Role;
import com.example.online.e_commerce.shopping.system.model.User;
import com.example.online.e_commerce.shopping.system.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute(GlobalControllerAdvice.SESSION_USER_KEY) != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = userService.authenticate(email.trim(), password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            session.setAttribute(GlobalControllerAdvice.SESSION_USER_KEY, user);
            redirectAttributes.addFlashAttribute("successMessage", "Welcome back, " + user.getName() + "!");
            if (user.isAdmin()) {
                return "redirect:/admin";
            }
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid email or password.");
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String registerPage(HttpSession session) {
        if (session.getAttribute(GlobalControllerAdvice.SESSION_USER_KEY) != null) {
            return "redirect:/";
        }
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@RequestParam("name") String name,
                                 @RequestParam("email") String email,
                                 @RequestParam("password") String password,
                                 @RequestParam(value = "phone", required = false) String phone,
                                 @RequestParam(value = "address", required = false) String address,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        try {
            User user = userService.registerUser(name, email.trim(), password, phone, address, Role.ROLE_CUSTOMER);
            session.setAttribute(GlobalControllerAdvice.SESSION_USER_KEY, user);
            redirectAttributes.addFlashAttribute("successMessage", "Account created successfully! Welcome to our store.");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.removeAttribute(GlobalControllerAdvice.SESSION_USER_KEY);
        redirectAttributes.addFlashAttribute("infoMessage", "You have been logged out.");
        return "redirect:/";
    }

    @GetMapping("/demo-login")
    public String quickDemoLogin(@RequestParam("role") String role,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        String email = "admin".equalsIgnoreCase(role) ? "admin@ecommerce.com" : "customer@ecommerce.com";
        Optional<User> userOpt = userService.getUserByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            session.setAttribute(GlobalControllerAdvice.SESSION_USER_KEY, user);
            redirectAttributes.addFlashAttribute("successMessage", "Logged in as demo " + role.toUpperCase() + " (" + user.getName() + ")!");
            if (user.isAdmin()) {
                return "redirect:/admin";
            }
        }
        return "redirect:/";
    }
}
