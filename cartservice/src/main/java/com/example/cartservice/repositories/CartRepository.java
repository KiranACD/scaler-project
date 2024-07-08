package com.example.cartservice.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.cartservice.models.Cart;

@Repository
public interface CartRepository extends MongoRepository<Cart, Long>{
    
    Optional<Cart> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
