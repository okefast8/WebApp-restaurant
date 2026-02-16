package com.example.webapp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private LocalDateTime createdAt;

    public Transaction() {
        this.createdAt = LocalDateTime.now();
    }

    // getters & setters

    public TransactionType getType() {
        return type;
    }
    public Long getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public void setType(TransactionType type) {
        this.type = type;
    }
}