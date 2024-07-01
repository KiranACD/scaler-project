package com.example.productservice.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.productservice.commons.AuthenticationCommons;
import com.example.productservice.dtos.Role;
import com.example.productservice.dtos.UserDTO;
import com.example.productservice.exceptions.CategoryNotFoundException;
import com.example.productservice.exceptions.ProductNotFoundException;
import com.example.productservice.models.Product;
import com.example.productservice.services.ProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;



@RestController
@RequestMapping("/products")   
public class ProductController {
    
    private ProductService productService;
    private AuthenticationCommons authenticationCommons;

    @Autowired
    public ProductController(@Qualifier("selfProductService") ProductService productService, AuthenticationCommons authenticationCommons) {
        this.productService = productService;
        this.authenticationCommons = authenticationCommons;
    }

    @GetMapping("")
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam("pageNumber") int pageNumber, 
                                                        @RequestParam("pageSize") int pageSize,
                                                        @RequestParam("sortBy") String sortBy,
                                                        @RequestParam("sortOrder") String sortOrder) {

        return new ResponseEntity<>(productService.getAllProducts(pageNumber, pageSize), HttpStatus.OK);
    }

    // Path should be the location of the resource being requested
    // This is why the id should be in the path and not in query params
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable("id") Long id) throws ProductNotFoundException {
        return productService.getProduct(id);
    }

    @GetMapping("/category/{category}")
    public Page<Product> getProductsInCategories(@PathVariable("category") String category) throws CategoryNotFoundException {
        return this.productService.getProductsInCategory(category);
    }

    @PostMapping()
    public Product addProduct(@RequestBody Product product) throws CategoryNotFoundException {
        return this.productService.addNewProduct(product);
    }

    @PatchMapping("/{id}")
    public Product updateProduct(@PathVariable("id") Long id, @RequestBody Product product) throws ProductNotFoundException {
        return productService.updateProduct(id, product);
    }

    @PutMapping("/{id}")
    public Product replaceProduct(@PathVariable("id") Long id, @RequestBody Product product) throws CategoryNotFoundException {
        return productService.replaceProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public Product deleteProduct(@PathVariable("id") Long id) throws ProductNotFoundException {
        return productService.deleteProduct(id);
    }

    // When exception is thrown from the controller, this class level handler will be 
    // called.
    @ExceptionHandler({ProductNotFoundException.class,
                      CategoryNotFoundException.class})
    public ResponseEntity<Void> handleException() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    




}
