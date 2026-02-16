package com.example.webapp.controller.menu;

import com.example.webapp.model.User;
import com.example.webapp.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserRepository userRepository;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {

        if (authentication == null ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("currentUser", user);

        return "menu";
    }
}
