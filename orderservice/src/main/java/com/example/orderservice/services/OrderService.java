package com.example.orderservice.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.orderservice.dtos.CartDTO;
import com.example.orderservice.dtos.CartItemDTO;
import com.example.orderservice.dtos.UpdateOrderDTO;
import com.example.orderservice.models.Order;
import com.example.orderservice.models.OrderItem;
import com.example.orderservice.repositories.OrderItemRepository;
import com.example.orderservice.repositories.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
    
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private RestTemplate restTemplate;

    @Value("${cart.service.url}")
    private String cartServiceURL;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, RestTemplate restTemplate) {
        
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public Order createOrder(Long userId) {
        
        CartDTO cart = getCartFromCartService(userId);

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is Empty");
        }

        Order order = createOrderFromCart(cart);
        Order savedOrder = orderRepository.save(order);

        String url = cartServiceURL + "/cart/" + userId;
        restTemplate.delete(url);

        return savedOrder;
    }

    public List<Order> getOrderHistory(Long userId) {

        List<Order> orders = orderRepository.findAllByUserId(userId);

        return orders;
    }

    public List<OrderItem> getOrderItems() {

        List<OrderItem> orderItems = orderItemRepository.findAll();

        return orderItems;
    }
  
    private CartDTO getCartFromCartService(Long userId) {
        
        String url = cartServiceURL + "/cart/" + userId;
        ResponseEntity<CartDTO> response = restTemplate.getForEntity(url, CartDTO.class);
        // System.out.println(cart.getUserId());
        // System.out.println(cart.getTotalPrice());
        // System.out.println(cart.getItems());
        return response.getBody();
    }

    private Order createOrderFromCart(CartDTO cart) {
        
        Order order = new Order();
        order.setUserId(cart.getUserId());
        order.setOrderDate(new Date());
        order.setOrderStatus("pending");
        order.setItems(new ArrayList<OrderItem>());

        for (CartItemDTO item: cart.getItems()) {
            OrderItem orderItem = createOrderItemFromCartItem(item);
            orderItem.setOrder(order); // Ensure the association is set. 
            order.getItems().add(orderItem);
        }

        order.setTotalPrice(cart.getTotalPrice());

        return order;
    }

    private OrderItem createOrderItemFromCartItem(CartItemDTO item) {

        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(item.getProductId());
        orderItem.setPrice(item.getPrice());
        orderItem.setQuantity(item.getQuantity());

        // OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        return orderItem;
    }

    public Order deleteOrder(Long id) {

        Order order = getOrder(id);

        if (order != null) {
            orderRepository.deleteById(id);
            return order;
        }

        return null;
        
    }

    public Order updateOrder(Long orderId, UpdateOrderDTO updateOrderDTO) {

        Order order = getOrder(orderId);

        if (order != null) {
            order.setOrderStatus(updateOrderDTO.getOrderStatus());
            Order updatedOrder = orderRepository.save(order);
            return updatedOrder;
        }

        return null;
    }

    public Order getOrder(Long orderId) {
        
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            return optionalOrder.get();
        }

        return null;
    }
}
