package com.example.cartservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cartservice.models.Cart;
import com.example.cartservice.models.CartItem;
import com.example.cartservice.services.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {
    
    private CartService cartService;

    public CartController(CartService cartService) {

        this.cartService = cartService;
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<String> addToCart(@PathVariable("id") Long userId, @RequestBody CartItem item) {

        Cart cart = cartService.addToCart(userId, item);
        return new ResponseEntity<>("Done", HttpStatus.ACCEPTED);
    }

    @PostMapping("/remove/{id}")
    public ResponseEntity<String> removeFromCart(@PathVariable("id") Long userId, @RequestBody CartItem item) {

        Cart cart = cartService.removeFromCart(userId, item);
        return new ResponseEntity<>("Done", HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable("id") Long userId) {

        Cart cart = cartService.getCart(userId);
        // System.out.println(cart.getUserId());
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Cart> deleteCart(@PathVariable("id") Long userId) {

        Cart cart = cartService.clearCart(userId);
        return new ResponseEntity<>(cart, HttpStatus.ACCEPTED); 
    }
}
