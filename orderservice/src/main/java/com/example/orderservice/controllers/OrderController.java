package com.example.orderservice.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orderservice.dtos.UpdateOrderDTO;
import com.example.orderservice.models.Order;
import com.example.orderservice.models.OrderItem;
import com.example.orderservice.services.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
    
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<Order> checkout(@PathVariable("userId") Long userId) {
        
        Order order = orderService.createOrder(userId);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Order>> getOrderHistory(@PathVariable("userId") Long userId) {
        
        List<Order> orders = orderService.getOrderHistory(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/orderItems")
    public ResponseEntity<List<OrderItem>> getOrderItems() {

        List<OrderItem> items = orderService.getOrderItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PutMapping("/status/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable("orderId") Long orderId, @RequestBody UpdateOrderDTO updateDTO) {
        
        Order updatedOrder = orderService.updateOrder(orderId, updateDTO);
        return new ResponseEntity<>(updatedOrder, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable("id") Long id) {

        Order order = orderService.deleteOrder(id);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }
}
