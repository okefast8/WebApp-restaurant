package com.example.webapp.controller.menu;

import com.example.webapp.model.MenuItemIngredient;
import com.example.webapp.repository.MenuItemIngredientRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuPublicApiController {

    private final MenuItemIngredientRepository repository;

    public MenuPublicApiController(MenuItemIngredientRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}/ingredients")
    public List<MenuItemIngredient> getIngredients(@PathVariable Long id) {
        return repository.findByMenuItemId(id);
    }
}
