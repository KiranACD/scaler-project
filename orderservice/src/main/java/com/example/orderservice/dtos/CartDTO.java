package com.example.orderservice.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDTO {
    private Long userId;
    private double totalPrice;
    private List<CartItemDTO> items;
}
