package com.example.orderservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.orderservice.models.Order;
import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Optional<Order> findById(Long id);
    List<Order> findAllByUserId(Long userId);
    void deleteById(Long id);
}
