package com.example.webapp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "menuItem",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Comment> comments;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String name;

    @Column(length = 1000)
    private String description;

    private BigDecimal price;

    private String imageUrl;

    @OneToMany(mappedBy = "menuItem",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<MenuItemIngredient> ingredients = new ArrayList<>();

    // ===== getters/setters =====

    public Long getId() { return id; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<MenuItemIngredient> getIngredients() { return ingredients; }
}
