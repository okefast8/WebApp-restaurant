package com.example.webapp.repository;

import com.example.webapp.model.Order;
import com.example.webapp.model.OrderStatus;
import com.example.webapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Для повара (NEW + ACCEPTED)
    List<Order> findByStatusInOrderByCreatedAtAsc(List<OrderStatus> statuses);

    // Для истории пользователя
    List<Order> findByUserOrderByCreatedAtDesc(User user);

    // Все заказы (например для админа или повара)
    List<Order> findAllByOrderByCreatedAtDesc();

    @Query("""
SELECT DISTINCT o FROM Order o
LEFT JOIN FETCH o.items i
LEFT JOIN FETCH i.removedIngredients
LEFT JOIN FETCH i.menuItem
LEFT JOIN FETCH o.user u
LEFT JOIN FETCH u.allergens
WHERE o.status IN :statuses
ORDER BY o.createdAt ASC
""")
    List<Order> findFullOrdersByStatusIn(
            @Param("statuses") List<OrderStatus> statuses
    );

}
