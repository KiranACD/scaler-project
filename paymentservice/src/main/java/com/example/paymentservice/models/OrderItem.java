package com.example.paymentservice.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItem {
    
    private Long id;
    private Long productId;
    private int quantity;
    private double price;
}
