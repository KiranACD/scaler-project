package com.example.userservice.repositories;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.userservice.models.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    
    Token save(Token token);

    Optional<Token> findByValueAndIsDeletedEquals(String value, boolean isDeleted);

    Optional<Token> findByValueAndIsDeletedEqualsAndExpiresAtGreaterThan(String value, boolean isDeleted, Date currentDate);
}
