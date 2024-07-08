package com.example.cartservice.services;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.cartservice.models.Cart;
import com.example.cartservice.models.CartItem;
import com.example.cartservice.repositories.CartRepository;
import com.example.cartservice.utils.RoundDouble;

@Service
public class CartService {
    
    private CartRepository cartRepository;
    private RedisTemplate<String, Cart> redisTemplate;
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String CART_CACHE_PREFIX = "CART:";

    public CartService(CartRepository cartRepository,
                       RedisTemplate<String, Cart> redisTemplate,
                       KafkaTemplate<String, String> kafkaTemplate) {

        this.cartRepository = cartRepository;
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
        
    }

    public Cart addToCart(Long userId, CartItem item) {
        
        Cart cart = getCart(userId);

        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setItems(new ArrayList<CartItem>());
        }

        boolean itemExists = false;
        double totalPrice = cart.getTotalPrice();
        int newQty = item.getQuantity();
        double newPrice = item.getPrice();
        double oldTotalPriceContr = 0.0;
        double newTotalPriceContr = RoundDouble.roundDouble(newPrice*newQty, 2);
        for (CartItem cartItem: cart.getItems()) {
            if (cartItem.getProductId().equals(item.getProductId())) {
                int oldQty = cartItem.getQuantity();
                double oldPrice = cartItem.getPrice();
                oldTotalPriceContr = RoundDouble.roundDouble(oldPrice*oldQty, 2);
                item.setQuantity(newQty);
                item.setPrice(newPrice);
                cart.setTotalPrice(totalPrice-oldTotalPriceContr+newTotalPriceContr);
                itemExists = true;
            }
        }

        if (!itemExists) {
            cart.getItems().add(item);
            cart.setTotalPrice(totalPrice+newTotalPriceContr);
        }

        cartRepository.save(cart);
        redisTemplate.opsForValue().set(CART_CACHE_PREFIX+userId, cart);
        redisTemplate.expire(CART_CACHE_PREFIX+userId, Duration.ofHours(2));

        return cart;
    }

    public Cart removeFromCart(Long userId, CartItem item) {

        Cart cart = getCart(userId);

        if (cart == null) {
            throw new RuntimeException();
        }

        Iterator<CartItem> iterator = cart.getItems().iterator();

        while (iterator.hasNext()) {
            CartItem cartItem = iterator.next();
            if (cartItem.getProductId().equals(item.getProductId())) {
                cartItem.setQuantity(cartItem.getQuantity() - item.getQuantity());
                if (cartItem.getQuantity() <= 0) {
                    iterator.remove();
                }
            }
        }

        double totalPrice = 0.0;
        for (CartItem cartItem: cart.getItems()) {
            totalPrice += RoundDouble.roundDouble(cartItem.getPrice() * cartItem.getQuantity(), 2);
        }

        cart.setTotalPrice(totalPrice);

        cartRepository.save(cart);
        redisTemplate.opsForValue().set(CART_CACHE_PREFIX+userId, cart);
        redisTemplate.expire(CART_CACHE_PREFIX+userId, Duration.ofHours(2));

        return cart;
    }

    public Cart getCart(Long userId) {

        Cart cachedCart = getCartFromCache(userId);
        if (cachedCart == null) {
            Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
            if (optionalCart.isPresent()) {
                Cart savedCart = optionalCart.get();
                if (savedCart.getItems() == null) savedCart.setItems(new ArrayList<CartItem>());
                return savedCart;
            }
            return null;
        }
        return cachedCart;
        // return Optional.ofNullable(redisTemplate.opsForValue().get(CART_CACHE_PREFIX+userId))
        //         .orElseGet(() -> cartRepository.findById(userId).orElse(new Cart()));
    }

    public Cart getCartFromCache(Long userId) {
        
        Cart cachedCart = redisTemplate.opsForValue().get(CART_CACHE_PREFIX+userId);
        // System.out.println("Cached Cart: " + cachedCart.getUserId());
        return cachedCart;
    }

    public Cart clearCart(Long userId) {

        Cart cart = getCart(userId);
        if (cart == null) {
            throw new RuntimeException();
        }
        redisTemplate.delete(CART_CACHE_PREFIX+userId);
        cartRepository.deleteByUserId(userId);
        return cart;
    }
}
