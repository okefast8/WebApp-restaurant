package com.example.webapp.service;

import com.example.webapp.model.CartItem;
import com.example.webapp.model.MenuItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CartService {

    private static final String CART_SESSION_KEY = "cart";

    private final HttpSession session;

    public CartService(HttpSession session) {
        this.session = session;
    }

    // ===============================
    // Получить корзину из сессии
    // ===============================
    @SuppressWarnings("unchecked")
    private List<CartItem> getCart() {

        Object cartObj = session.getAttribute(CART_SESSION_KEY);

        // Если в сессии ничего нет
        if (cartObj == null) {
            List<CartItem> newCart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, newCart);
            return newCart;
        }

        // Если вдруг там старая Map (от прошлой версии)
        if (cartObj instanceof Map) {
            List<CartItem> newCart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, newCart);
            return newCart;
        }

        return (List<CartItem>) cartObj;
    }

    // ===============================
    // Добавить товар (с кастомизацией)
    // ===============================
    public void add(MenuItem menuItem, Set<Long> removedIds) {

        List<CartItem> cart = getCart();

        for (CartItem item : cart) {

            boolean sameMenuItem =
                    item.getMenuItemId().equals(menuItem.getId());

            boolean sameRemoved =
                    Objects.equals(
                            item.getRemovedIngredientIds(),
                            removedIds
                    );

            if (sameMenuItem && sameRemoved) {
                item.increaseQuantity();
                return;
            }
        }

        CartItem newItem = new CartItem(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getPrice()
        );

        newItem.setRemovedIngredientIds(
                removedIds != null
                        ? new HashSet<>(removedIds)
                        : new HashSet<>()
        );

        cart.add(newItem);
    }

    // ===============================
    // Добавить без кастомизации
    // ===============================
    public void add(MenuItem menuItem) {
        add(menuItem, new HashSet<>());
    }

    // ===============================
    // Удалить позицию по cartItemId
    // ===============================
    public void remove(UUID cartId) {

        List<CartItem> cart = getCart();

        cart.removeIf(item ->
                item.getCartId().equals(cartId)
        );
    }

    // ===============================
    // Получить список товаров
    // ===============================
    public List<CartItem> getItems() {
        return getCart();
    }

    // ===============================
    // Получить общую сумму
    // ===============================
    public BigDecimal getTotal() {
        return getCart().stream()
                .map(CartItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ===============================
    // Очистить корзину
    // ===============================
    public void clear() {
        session.removeAttribute(CART_SESSION_KEY);
    }

    // ===============================
    // Проверка пустая ли корзина
    // ===============================
    public boolean isEmpty() {
        return getCart().isEmpty();
    }
}
