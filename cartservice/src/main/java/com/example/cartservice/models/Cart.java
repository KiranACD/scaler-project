package com.example.cartservice.models;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "carts")
@Getter
@Setter
public class Cart implements Serializable {
    
    private Long userId;
    private List<CartItem> items;
    private double totalPrice;
}
