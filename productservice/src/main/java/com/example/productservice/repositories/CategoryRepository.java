package com.example.productservice.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.productservice.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Category save(Category category);

    Optional<Category> findByName(String categoryName);

    List<Category> findAll();
}
