package com.example.productservice.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.productservice.dtos.FakeStoreProductDto;
import com.example.productservice.exceptions.CategoryNotFoundException;
import com.example.productservice.exceptions.ProductNotFoundException;
import com.example.productservice.models.Product;

public interface ProductService {
    
    Product getProduct(Long id) throws ProductNotFoundException;

    Page<Product> getAllProducts(int pageNumber, int pageSize);

    Page<Product> getProductsInCategory(String category) throws CategoryNotFoundException;

    Product addNewProduct(Product product) throws CategoryNotFoundException;

    Product replaceProduct(Long id, Product product) throws CategoryNotFoundException;

    Product updateProduct(Long id, Product product) throws ProductNotFoundException;

    Product deleteProduct(Long id) throws ProductNotFoundException;
    
}
