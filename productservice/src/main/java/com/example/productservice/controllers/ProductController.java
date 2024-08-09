package com.example.productservice.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.productservice.exceptions.CategoryNotFoundException;
import com.example.productservice.exceptions.ProductNotFoundException;
import com.example.productservice.models.Product;
import com.example.productservice.models.ProductDocument;
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


@RestController
@RequestMapping("/products")   
public class ProductController {
    
    private ProductService productService;

    @Autowired
    public ProductController(@Qualifier("fakeStoreProductService") ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam("pageNumber") int pageNumber, 
                                                        @RequestParam("pageSize") int pageSize,
                                                        @RequestParam("sortBy") String sortBy,
                                                        @RequestParam("sortOrder") String sortOrder) {

        return new ResponseEntity<>(productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    // Path should be the location of the resource being requested
    // This is why the id should be in the path and not in query params
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable("id") Long id) throws ProductNotFoundException {
        return productService.getProduct(id);
    }

    @GetMapping("/seed")
    public ResponseEntity<String> seedDatabase() {
        productService.seedRepositories();
        return new ResponseEntity<>("done", HttpStatus.OK);
    }

    @GetMapping("/seedElasticsearch")
    public ResponseEntity<String> seedElasticSearch() {
        productService.seedElasticsearch();
        return new ResponseEntity<>("done", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDocument>> searchProducts(@RequestParam("searchString") String searchString) {
        List<ProductDocument> productDocuments = productService.searchProducts(searchString);
        return new ResponseEntity<>(productDocuments, HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    public Page<Product> getProductsInCategories(@PathVariable("category") String category) throws CategoryNotFoundException {
        return this.productService.getProductsInCategory(category);
    }

    @PostMapping()
    public ResponseEntity<Product> addProduct(@RequestBody Product product) throws CategoryNotFoundException {
        Product savedProduct = this.productService.addNewProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) throws ProductNotFoundException {
        Product updatedProduct = productService.updateProduct(id, product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> replaceProduct(@PathVariable("id") Long id, @RequestBody Product product) throws CategoryNotFoundException {
        Product replacedProduct = productService.replaceProduct(id, product);
        return new ResponseEntity<>(replacedProduct, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long id) throws ProductNotFoundException {
        Product deletedProduct = productService.deleteProduct(id);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }

    // When exception is thrown from the controller, this class level handler will be 
    // called.
    @ExceptionHandler({ProductNotFoundException.class,
                      CategoryNotFoundException.class})
    public ResponseEntity<Void> handleException() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    




}
