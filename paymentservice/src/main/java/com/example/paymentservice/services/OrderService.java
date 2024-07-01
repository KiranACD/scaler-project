package com.example.paymentservice.services;

import org.springframework.stereotype.Service;

import com.example.paymentservice.models.Order;

@Service
public class OrderService {
    
    public Order getOrder(String orderId) {
        return new Order();
    }
}
