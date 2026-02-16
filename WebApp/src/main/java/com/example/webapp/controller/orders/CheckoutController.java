package com.example.webapp.controller.orders;

import com.example.webapp.model.*;
import com.example.webapp.repository.IngredientRepository;
import com.example.webapp.repository.OrderRepository;
import com.example.webapp.service.CartService;
import com.example.webapp.service.UserService;
import com.example.webapp.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
public class CheckoutController {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final PaymentService paymentService;
    private final IngredientRepository ingredientRepository;

    public CheckoutController(CartService cartService,
                              OrderRepository orderRepository,
                              UserService userService,
                              PaymentService paymentService, IngredientRepository ingredientRepository) {
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.paymentService = paymentService;
        this.ingredientRepository = ingredientRepository;
    }

    @PostMapping("/checkout")
    public String checkout() {

        User user = userService.getCurrentUser();
        List<CartItem> cartItems = cartService.getItems();

        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        BigDecimal total = cartService.getTotal();

        try {
            // 💳 Списание через PaymentService (создаёт транзакцию)
            paymentService.pay(user, total);

        } catch (RuntimeException e) {
            return "redirect:/cart?error=notenoughmoney";
        }

        // 📦 Создание заказа
        Order order = new Order();
        order.setUser(user);
        order.setTotal(total);
        order.setStatus(OrderStatus.NEW);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setName(cartItem.getName());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());

            if (cartItem.getRemovedIngredientIds() != null &&
                    !cartItem.getRemovedIngredientIds().isEmpty()) {

                List<Ingredient> removed =
                        ingredientRepository.findAllById(
                                cartItem.getRemovedIngredientIds()
                        );

                orderItem.setRemovedIngredients(new HashSet<>(removed));
            }

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);

        orderRepository.save(order);

        // 🧹 Очистка корзины
        cartService.clear();

        return "redirect:/menu?success=paid";
    }
}
