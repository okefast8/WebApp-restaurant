package com.example.webapp.controller.orders;

import com.example.webapp.model.Order;
import com.example.webapp.model.OrderStatus;
import com.example.webapp.model.User;
import com.example.webapp.repository.OrderRepository;
import com.example.webapp.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderRestController(OrderRepository orderRepository,
                               UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/orders")
    @Transactional(readOnly = true)
    public List<Order> getChefOrders() {

        return orderRepository.findFullOrdersByStatusIn(
                List.of(OrderStatus.NEW, OrderStatus.ACCEPTED)
        );
    }
}