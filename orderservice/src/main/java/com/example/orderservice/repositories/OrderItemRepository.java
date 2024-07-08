package com.example.orderservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.orderservice.models.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
}
