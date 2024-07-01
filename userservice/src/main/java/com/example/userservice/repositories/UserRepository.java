package com.example.userservice.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.userservice.models.User;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    
    List<User> findAll();
    Optional<User> findById(Long id);
    User save(User user);
    Optional<User> findByEmail(String email);
    void deleteById(Long id);
}
