package com.example.webapp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Ingredient ingredient;

    @Column(nullable = false)
    private BigDecimal quantity = BigDecimal.ZERO;

    public Long getId() { return id; }

    public Ingredient getIngredient() { return ingredient; }
    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
}
