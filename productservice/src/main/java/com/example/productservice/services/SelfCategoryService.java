package com.example.productservice.services;

import java.util.List;

import com.example.productservice.models.Category;
import com.example.productservice.repositories.CategoryRepository;

public class SelfCategoryService implements CategoryService{

    private CategoryRepository categoryRepository;

    public SelfCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }


    
}
