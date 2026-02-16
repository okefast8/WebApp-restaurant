package com.example.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/register", "/login").permitAll()

                        // 🔥 ПУБЛИЧНЫЙ API СОСТАВА
                        .requestMatchers("/api/menu/**")
                        .hasAnyRole("USER", "ADMIN", "CHEF")

                        // ADMIN
                        .requestMatchers("/menu/admin/**").hasRole("ADMIN")

                        // CHEF
                        .requestMatchers("/chef/**").hasRole("CHEF")

                        // MENU
                        .requestMatchers("/menu/**")
                        .hasAnyRole("USER", "ADMIN", "CHEF")

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {

                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                            boolean isChef = authentication.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_CHEF"));

                            if (isAdmin) {
                                response.sendRedirect("/menu/admin");
                            } else if (isChef) {
                                response.sendRedirect("/chef");
                            } else {
                                response.sendRedirect("/menu");
                            }
                        })
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        http.headers(headers ->
                headers.frameOptions(frame -> frame.disable())
        );


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}