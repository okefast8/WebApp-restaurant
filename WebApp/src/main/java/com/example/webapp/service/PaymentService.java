package com.example.webapp.service;

import com.example.webapp.model.Transaction;
import com.example.webapp.model.TransactionType;
import com.example.webapp.model.User;
import com.example.webapp.repository.TransactionRepository;
import com.example.webapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public PaymentService(UserRepository userRepository,
                          TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    // Пополнение
    public void topUp(User user, BigDecimal amount) {

        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.TOP_UP);

        transactionRepository.save(transaction);
    }

    // Списание при оплате заказа
    public void pay(User user, BigDecimal amount) {

        if (user.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Недостаточно средств");
        }

        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(amount.negate()); // минус
        transaction.setType(TransactionType.PAYMENT);

        transactionRepository.save(transaction);
    }

    // Изменение админом
    public void adminChange(User user, BigDecimal amount) {

        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.ADMIN_CHANGE);

        transactionRepository.save(transaction);
    }
}
