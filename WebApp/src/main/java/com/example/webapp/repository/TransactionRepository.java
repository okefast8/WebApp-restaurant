package com.example.webapp.repository;

import com.example.webapp.model.Transaction;
import com.example.webapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserOrderByCreatedAtDesc(User user);
}