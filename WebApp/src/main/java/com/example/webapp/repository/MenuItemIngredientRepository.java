package com.example.webapp.repository;

import com.example.webapp.model.MenuItemIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemIngredientRepository
        extends JpaRepository<MenuItemIngredient, Long> {

    List<MenuItemIngredient> findByMenuItemId(Long menuItemId);

    void deleteByMenuItemId(Long menuItemId);
}
