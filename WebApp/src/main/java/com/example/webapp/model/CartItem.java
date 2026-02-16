package com.example.webapp.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CartItem {

    private UUID cartId;          // Уникальный ID позиции
    private Long menuItemId;      // ID блюда
    private String name;
    private BigDecimal price;
    private int quantity;
    private List<Ingredient> removedIngredients;
    private Set<Long> removedIngredientIds = new HashSet<>();

    public CartItem(Long menuItemId, String name, BigDecimal price) {
        this.cartId = UUID.randomUUID();
        this.menuItemId = menuItemId;
        this.name = name;
        this.price = price;
        this.quantity = 1;
    }

    public CartItem() {}

    public boolean hasRemovedIngredients() {
        return removedIngredientIds != null && !removedIngredientIds.isEmpty();
    }

    public List<Ingredient> getRemovedIngredients() {
        return removedIngredients;
    }

    public void setRemovedIngredients(List<Ingredient> removedIngredients) {
        this.removedIngredients = removedIngredients;
    }


    public UUID getCartId() {
        return cartId;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Set<Long> getRemovedIngredientIds() {
        return removedIngredientIds;
    }

    public void setRemovedIngredientIds(Set<Long> removedIngredientIds) {
        this.removedIngredientIds = removedIngredientIds;
    }

    public void increaseQuantity() {
        this.quantity++;
    }

    public BigDecimal getTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
