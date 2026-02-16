package com.example.webapp.controller.menu;

import com.example.webapp.model.Category;
import com.example.webapp.model.Ingredient;
import com.example.webapp.model.MenuItem;
import com.example.webapp.model.MenuItemIngredient;
import com.example.webapp.repository.IngredientRepository;
import com.example.webapp.repository.MenuItemIngredientRepository;
import com.example.webapp.repository.MenuItemRepository;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/menu/admin")
public class AdminMenuController {

    private final MenuItemRepository menuItemRepository;
    private final IngredientRepository ingredientRepository;
    private final MenuItemIngredientRepository menuItemIngredientRepository;

    public AdminMenuController(MenuItemRepository menuItemRepository,
                               IngredientRepository ingredientRepository,
                               MenuItemIngredientRepository menuItemIngredientRepository) {
        this.menuItemRepository = menuItemRepository;
        this.ingredientRepository = ingredientRepository;
        this.menuItemIngredientRepository = menuItemIngredientRepository;
    }

    // =====================================================
    //                 ГЛАВНАЯ СТРАНИЦА
    // =====================================================

    @GetMapping
    public String adminMenu(Model model) {

        model.addAttribute("menuItems", menuItemRepository.findAll());
        model.addAttribute("ingredients", ingredientRepository.findAll());
        model.addAttribute("categories", Category.values());

        return "admin_menu";
    }

    // =====================================================
    //                 ДОБАВЛЕНИЕ БЛЮДА
    // =====================================================

    @PostMapping("/add")
    public String addMenuItem(@RequestParam String name,
                              @RequestParam String description,
                              @RequestParam BigDecimal price,
                              @RequestParam Category category,
                              @RequestParam(required = false) String imageUrl) {

        MenuItem item = new MenuItem();
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        item.setCategory(category);
        item.setImageUrl(imageUrl);

        menuItemRepository.save(item);

        return "redirect:/menu/admin";
    }

    // =====================================================
    //                 УДАЛЕНИЕ БЛЮДА
    // =====================================================

    @PostMapping("/delete/{id}")
    @Transactional
    public String deleteMenuItem(@PathVariable Long id) {

        menuItemIngredientRepository.deleteByMenuItemId(id);
        menuItemRepository.deleteById(id);

        return "redirect:/menu/admin";
    }

    // =====================================================
    //                 ДОБАВЛЕНИЕ ИНГРЕДИЕНТА НА СКЛАД
    // =====================================================

    @PostMapping("/ingredient/add")
    public String addIngredientToStorage(@RequestParam String name,
                                         @RequestParam double quantity,
                                         @RequestParam String unit) {

        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setStockQuantity(quantity);
        ingredient.setUnit(unit);

        ingredientRepository.save(ingredient);

        return "redirect:/menu/admin";
    }

    // =====================================================
    //                 УДАЛЕНИЕ ИНГРЕДИЕНТА СО СКЛАДА
    // =====================================================

    @PostMapping("/ingredient/delete/{id}")
    public String deleteIngredientFromStorage(@PathVariable Long id) {

        ingredientRepository.deleteById(id);
        return "redirect:/menu/admin";
    }

    // =====================================================
    //                 ПОЛУЧЕНИЕ СОСТАВА
    // =====================================================

    @GetMapping("/ingredients/{menuItemId}")
    @ResponseBody
    public List<MenuItemIngredient> getIngredients(@PathVariable Long menuItemId) {

        return menuItemIngredientRepository.findByMenuItemId(menuItemId);
    }

    // =====================================================
    //                 ДОБАВЛЕНИЕ В СОСТАВ
    // =====================================================

    @PostMapping("/ingredients/add")
    @ResponseBody
    public void addIngredient(@RequestParam Long menuItemId,
                              @RequestParam Long ingredientId,
                              @RequestParam double quantity) {

        MenuItem menuItem =
                menuItemRepository.findById(menuItemId).orElseThrow();

        Ingredient ingredient =
                ingredientRepository.findById(ingredientId).orElseThrow();

        MenuItemIngredient link = new MenuItemIngredient();
        link.setMenuItem(menuItem);
        link.setIngredient(ingredient);
        link.setQuantity(quantity);

        menuItemIngredientRepository.save(link);
    }

    // =====================================================
    //                 УДАЛЕНИЕ ИЗ СОСТАВА
    // =====================================================

    @DeleteMapping("/ingredients/delete/{id}")
    @ResponseBody
    public void deleteIngredient(@PathVariable Long id) {

        menuItemIngredientRepository.deleteById(id);
    }
}
