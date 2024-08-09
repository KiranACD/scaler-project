package com.example.paymentservice.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    private Long id;
    private Long userId;
    private double totalPrice;
    private String orderStatus;
    private String userName;
    private String email;
    private List<OrderItem> items;
}
