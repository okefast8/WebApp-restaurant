package com.example.webapp.controller.transactions;

import com.example.webapp.model.User;
import com.example.webapp.repository.UserRepository;
import com.example.webapp.service.PaymentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@Controller
public class BalanceController {

    private final UserRepository userRepository;
    private final PaymentService paymentService;

    public BalanceController(UserRepository userRepository,
                             PaymentService paymentService) {
        this.userRepository = userRepository;
        this.paymentService = paymentService;
    }

    @PostMapping("/balance/topup")
    public String topUp(@RequestParam BigDecimal amount,
                        Principal principal) {

        User user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        paymentService.topUp(user, amount);
        return "redirect:/menu";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/balance")
    public String changeBalance(@RequestParam Long userId,
                                @RequestParam BigDecimal amount) {

        User user = userRepository.findById(userId).orElseThrow();
        paymentService.adminChange(user, amount);

        return "redirect:/admin";
    }
}
