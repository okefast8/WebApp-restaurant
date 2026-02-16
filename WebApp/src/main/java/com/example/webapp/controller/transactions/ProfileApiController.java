package com.example.webapp.controller.transactions;

import com.example.webapp.model.User;
import com.example.webapp.repository.OrderRepository;
import com.example.webapp.repository.TransactionRepository;
import com.example.webapp.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProfileApiController {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;

    public ProfileApiController(UserRepository userRepository,
                                OrderRepository orderRepository,
                                TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/profile")
    public Map<String, Object> getProfile(Authentication authentication) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        return Map.of(
                "balance", user.getBalance(),
                "orders", orderRepository.findByUserOrderByCreatedAtDesc(user),
                "transactions", transactionRepository.findByUserOrderByCreatedAtDesc(user)
        );
    }
}
