package com.example.productservice.services;

import java.util.List;

import com.example.productservice.models.Category;
import com.example.productservice.models.Product;

public interface CategoryService {
    
    List<Category> getCategories();
}
