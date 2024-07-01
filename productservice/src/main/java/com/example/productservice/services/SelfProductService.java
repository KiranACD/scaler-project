package com.example.productservice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.productservice.exceptions.CategoryNotFoundException;
import com.example.productservice.exceptions.ProductNotFoundException;
import com.example.productservice.models.Category;
import com.example.productservice.models.Product;
import com.example.productservice.repositories.CategoryRepository;
import com.example.productservice.repositories.ProductRepository;

@Service("selfProductService")
public class SelfProductService implements ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public SelfProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        // TODO Auto-generated method stub
        Optional<Product> productOptional = findProductById(id);

        if (productOptional.isEmpty()) {
            throw new ProductNotFoundException("Prodict with id:" + id + " does not exist.");
        }

        Product product = productOptional.get();

        return product;
    }

    @Override
    public Page<Product> getAllProducts(int pageNumber, int pageSize) {
        // TODO Auto-generated method stub
        Page<Product> products = productRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return products;
    }

    @Override
    public Page<Product> getProductsInCategory(String categoryName) throws CategoryNotFoundException {
        // TODO Auto-generated method stub
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
        
        if (categoryOptional.isEmpty()) {
            throw new CategoryNotFoundException("" + categoryName + " not a category.");
        }

        Category category = categoryOptional.get();
        Page<Product> products = productRepository.findAllByCategory(category, PageRequest.of(0, 0));

        return products;
    }

    @Override
    public Product addNewProduct(Product product) throws CategoryNotFoundException {
        // TODO Auto-generated method stub
        // The category in the product may or may not be saved in the database yet.
        // If the category object does not have an id, then the category is not saved..
        // ... in the database.
        Category category = product.getCategory();
        if (category != null && category.getId() == null) {
            Category savedCategory = categoryRepository.save(category);
            product.setCategory(savedCategory);
        }
        if (category == null) {
            throw new CategoryNotFoundException("Category not provided.");
        }
        return saveProduct(product);
    }

    @Override
    public Product replaceProduct(Long id, Product product) throws CategoryNotFoundException {
        // TODO Auto-generated method stub
        Optional<Product> productOptional = findProductById(id);
        if (productOptional.isPresent()) {
            Product savedProduct = productOptional.get();
            savedProduct = updateProductAttributes(product, savedProduct);
            return saveProduct(savedProduct);
        }
        return addNewProduct(product);
    }

    private Product updateProductAttributes(Product newProduct, Product oldProduct) {
        if (newProduct.getTitle() != null) {
            oldProduct.setTitle(newProduct.getTitle());
        }
        if (newProduct.getDescription() != null) {
            oldProduct.setDescription(newProduct.getDescription());
        }
        if (newProduct.getPrice() != null) {
            oldProduct.setPrice(newProduct.getPrice());
        }
        if (newProduct.getImageUrl() != null) {
            oldProduct.setImageUrl(newProduct.getImageUrl());
        }
        if (newProduct.getCategory() != null) {
            oldProduct.setCategory(newProduct.getCategory());
        }
        return oldProduct;
    }

    @Override
    public Product updateProduct(Long id, Product product) throws ProductNotFoundException {
        // TODO Auto-generated method stub
        Optional<Product> productOptional = findProductById(id);
        if (productOptional.isEmpty()) throw new ProductNotFoundException("Product with id:" + id + " not found.");
        Product savedProduct = productOptional.get();
        savedProduct = updateProductAttributes(product, savedProduct);
        return saveProduct(savedProduct);
    }

    @Override
    public Product deleteProduct(Long id) throws ProductNotFoundException {
        // TODO Auto-generated method stub
        Optional<Product> productOptional = findProductById(id);
        if (productOptional.isEmpty()) throw new ProductNotFoundException("Product with id:" + id + " not found.");
        Product product = productOptional.get();
        productRepository.deleteById(id);
        return product;
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }
    
    
}
