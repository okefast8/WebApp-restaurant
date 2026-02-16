package com.example.webapp.controller.chef;

import com.example.webapp.model.*;
import com.example.webapp.repository.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chef")
public class ChefApiController {

    private final OrderRepository orderRepository;
    private final IngredientRepository ingredientRepository;
    private final MenuItemRepository menuItemRepository;

    public ChefApiController(OrderRepository orderRepository,
                             IngredientRepository ingredientRepository,
                             MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.ingredientRepository = ingredientRepository;
        this.menuItemRepository = menuItemRepository;
    }

    // =========================
    //         ЗАКАЗЫ
    // =========================

    @GetMapping("/orders")
    @Transactional(readOnly = true)
    public List<Order> getChefOrders() {

        return orderRepository.findFullOrdersByStatusIn(
                List.of(OrderStatus.NEW, OrderStatus.ACCEPTED)
        );
    }

    // ===== ПРИНЯТЬ ЗАКАЗ =====
    @Transactional
    @PostMapping("/accept/{id}")
    public void acceptOrder(@PathVariable Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));

        if (order.getStatus() != OrderStatus.NEW) return;

        Set<Allergen> userAllergens = order.getUser().getAllergens();

        for (OrderItem item : order.getItems()) {

            MenuItem menuItem = menuItemRepository
                    .findByNameWithIngredients(item.getName())
                    .orElseThrow(() ->
                            new RuntimeException("Блюдо не найдено"));

            int orderedQuantity = item.getQuantity();

            // 🔥 Получаем ID удалённых ингредиентов
            Set<Long> removedIds = item.getRemovedIngredients()
                    .stream()
                    .map(Ingredient::getId)
                    .collect(Collectors.toSet());

            for (MenuItemIngredient mii : menuItem.getIngredients()) {

                Ingredient ingredient = mii.getIngredient();

                // 1️⃣ Если ингредиент вручную убрали — пропускаем
                if (removedIds.contains(ingredient.getId())) {
                    continue;
                }

                // 2️⃣ Проверка аллергенов пользователя
                boolean hasAllergen = ingredient.getAllergens()
                        .stream()
                        .anyMatch(userAllergens::contains);

                if (hasAllergen) continue;

                double required = mii.getQuantity() * orderedQuantity;

                if (ingredient.getQuantity() < required) {
                    throw new RuntimeException(
                            "Недостаточно ингредиента: "
                                    + ingredient.getName()
                    );
                }

                ingredient.setStockQuantity(
                        ingredient.getQuantity() - required
                );

                ingredientRepository.save(ingredient);
            }
        }


        order.setStatus(OrderStatus.ACCEPTED);
        orderRepository.save(order);
    }

    // ===== ГОТОВО =====
    @PostMapping("/ready/{id}")
    public void markReady(@PathVariable Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));

        order.setStatus(OrderStatus.READY);
        orderRepository.save(order);
    }

    // =========================
    //         СКЛАД
    // =========================

    @GetMapping("/warehouse")
    public List<Ingredient> getWarehouse() {
        return ingredientRepository.findAllByOrderByNameAsc();
    }

    // ===== ПОПОЛНЕНИЕ =====
    @PostMapping("/warehouse/add/{id}")
    public void addStock(@PathVariable Long id,
                         @RequestBody StockRequest request) {

        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Ингредиент не найден"));

        ingredient.setStockQuantity(
                (double) (ingredient.getQuantity() + request.getAmount())
        );

        ingredientRepository.save(ingredient);
    }
}
