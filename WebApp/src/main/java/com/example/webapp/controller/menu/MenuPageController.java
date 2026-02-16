package com.example.webapp.controller.menu;

import com.example.webapp.model.User;
import com.example.webapp.repository.MenuItemRepository;
import com.example.webapp.repository.OrderRepository;
import com.example.webapp.repository.TransactionRepository;
import com.example.webapp.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MenuPageController {

    private final MenuItemRepository menuRepo;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final OrderRepository orderRepository;

    public MenuPageController(MenuItemRepository menuRepo,
                              UserRepository userRepository,
                              TransactionRepository transactionRepository,
                              OrderRepository orderRepository) {
        this.menuRepo = menuRepo;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/menu")
    public String menuPage(Model model, Principal principal) {

        // Передаём список блюд
        model.addAttribute("menuItems", menuRepo.findAll());

        if (principal != null) {

            User user = userRepository
                    .findByUsername(principal.getName())
                    .orElse(null);

            if (user != null) {

                // текущий пользователь
                model.addAttribute("currentUser", user);

                // история транзакций
                model.addAttribute("transactions",
                        transactionRepository
                                .findByUserOrderByCreatedAtDesc(user));

                // история заказов пользователя
                model.addAttribute("orders",
                        orderRepository
                                .findByUserOrderByCreatedAtDesc(user));
            }
        }

        return "menu";
    }
}