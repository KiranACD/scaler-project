package com.example.productservice.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.productservice.models.Category;

@Service
public class FakeStoreCategoryService implements CategoryService {

    private RestTemplate restTemplate;

    public FakeStoreCategoryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public List<Category> getCategories() {
        String[] response = this.restTemplate.getForObject("https://fakestoreapi.com/products/categories", String[].class);
        List<Category> categories = new ArrayList<>();
        int i = 0;
        for (String c: response) {
            categories.add(convertFakeStoreCategoryDtoToCategory(Long.valueOf(++i), c));
        }
        return categories;
    }
    
    private Category convertFakeStoreCategoryDtoToCategory(Long id, String c) {
        Category category = new Category();
        category.setId(id);
        category.setName(c);
        return category;
    }
}
