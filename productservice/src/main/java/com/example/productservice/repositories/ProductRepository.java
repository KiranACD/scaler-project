package com.example.productservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.productservice.models.Category;
import com.example.productservice.models.Product;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Product save(Product product);

    Optional<Product> findById(Long id);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findAllByCategory(Category category, Pageable pageable);

    void deleteById(Long id);
}
