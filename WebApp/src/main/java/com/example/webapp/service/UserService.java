package com.example.webapp.service;

import com.example.webapp.model.User;
import com.example.webapp.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String username = auth.getName();

        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
}
