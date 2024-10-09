package com.example.FashionShop.repository;

import com.example.FashionShop.enums.OrderStatus;
import com.example.FashionShop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByOrderStatus(OrderStatus orderStatus);

    @Query("SELECT o FROM Order o WHERE o.orderStatus = :status AND o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findByOrderStatusAndDateBetween(@Param("status") OrderStatus status,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);
}
