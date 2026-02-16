package com.example.webapp.controller.menu;

import com.example.webapp.model.MenuItem;
import com.example.webapp.repository.MenuItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu") // <--- меняем путь
public class MenuController {

    private final MenuItemRepository menuRepo;

    public MenuController(MenuItemRepository menuRepo) {
        this.menuRepo = menuRepo;
    }

    @GetMapping
    public List<MenuItem> menu() {
        return menuRepo.findAll(); // JSON
    }
}

