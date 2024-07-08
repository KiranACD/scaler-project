package com.example.cartservice.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItem implements Serializable {
    private Long productId;
    private int quantity;
    private double price;
}
