package com.example.webapp.controller.orders;

import com.example.webapp.model.CartItem;
import com.example.webapp.model.Ingredient;
import com.example.webapp.model.MenuItem;
import com.example.webapp.repository.IngredientRepository;
import com.example.webapp.repository.MenuItemRepository;
import com.example.webapp.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final MenuItemRepository menuRepo;
    private final IngredientRepository ingredientRepository;

    public CartController(CartService cartService,
                          MenuItemRepository menuRepo,
                          IngredientRepository ingredientRepository) {
        this.cartService = cartService;
        this.menuRepo = menuRepo;
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping
    public String viewCart(Model model) {

        List<CartItem> items = cartService.getItems();

        for (CartItem item : items) {

            if (item.getRemovedIngredientIds() != null &&
                    !item.getRemovedIngredientIds().isEmpty()) {

                List<Ingredient> removed =
                        ingredientRepository.findAllById(
                                item.getRemovedIngredientIds()
                        );

                item.setRemovedIngredients(removed);
            }
        }

        model.addAttribute("cartItems", items);
        model.addAttribute("total", cartService.getTotal());

        return "cart";
    }

    @PostMapping("/add-custom")
    @ResponseBody
    public void addCustom(@RequestBody Map<String, Object> body) {

        Long menuItemId =
                Long.valueOf(body.get("menuItemId").toString());

        Object removedObj = body.get("removedIngredientIds");

        Set<Long> removedIds = new HashSet<>();

        if (removedObj instanceof List<?>) {
            for (Object id : (List<?>) removedObj) {
                removedIds.add(Long.valueOf(id.toString()));
            }
        }

        MenuItem menuItem = menuRepo.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Блюдо не найдено"));

        cartService.add(menuItem, removedIds);
    }


    @PostMapping("/add/{menuId}")
    public String addToCart(@PathVariable Long menuId) {

        MenuItem menuItem = menuRepo.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Блюдо не найдено"));

        cartService.add(menuItem);

        return "redirect:/menu";
    }

    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable UUID id) {

        cartService.remove(id);

        return "redirect:/cart";
    }
}
