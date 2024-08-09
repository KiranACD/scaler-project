package com.example.orderservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem extends BaseModel {
    
    private Long productId;
    private int quantity;
    private double price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="order_id")
    @JsonIgnore
    private Order order;
}
