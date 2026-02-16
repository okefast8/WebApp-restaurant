package com.example.webapp.repository;

import com.example.webapp.model.MenuItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    @Query("""
        SELECT m FROM MenuItem m
        LEFT JOIN FETCH m.ingredients i
        LEFT JOIN FETCH i.ingredient
        WHERE m.name = :name
    """)
    Optional<MenuItem> findByNameWithIngredients(@Param("name") String name);
}
