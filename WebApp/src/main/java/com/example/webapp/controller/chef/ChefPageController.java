package com.example.webapp.controller.chef;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChefPageController {

    @GetMapping("/chef")
    public String chefPage() {
        return "chef"; // имя HTML файла БЕЗ .html
    }
}
