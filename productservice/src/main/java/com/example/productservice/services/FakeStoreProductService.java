package com.example.productservice.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import com.example.productservice.dtos.FakeStoreProductDto;
import com.example.productservice.exceptions.ProductNotFoundException;
import com.example.productservice.models.Category;
import com.example.productservice.models.Product;
// import com.fasterxml.jackson.databind.ObjectMapper;

@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService{

    private RestTemplate restTemplate;
    private RedisTemplate<String, Product> redisTemplate;
    private ListOperations<String, Product> listOperations;

    @Autowired
    public FakeStoreProductService(RestTemplate restTemplate, RedisTemplate<String, Product> redisTemplate) {
        // The object of rest template is autowired to the constructor
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
        this.listOperations = redisTemplate.opsForList();
    }
    
    public Product getProduct(Long id) throws ProductNotFoundException {
        FakeStoreProductDto productDto = restTemplate.getForObject(
            "https://fakestoreapi.com/products/" + id,
            FakeStoreProductDto.class);
        
            if (productDto == null) {
                throw new ProductNotFoundException(
                    "Product with id: " + id + " not found."
                );
            }
            
        return convertFakeStoreProductDtoToProduct(productDto);
    }

    public Page<Product> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortOrder) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int start = (int) pageable.getOffset();
        // List<Product> products = listOperations.range("ProductList", 0, -1);

        // if (products == null || products.isEmpty()) {
        //     products = getAllProductsFromFakeStore();
        //     // FakeStoreProductDto[] response = restTemplate.getForObject("https://fakestoreapi.com/products/", FakeStoreProductDto[].class);
        //     // products = new ArrayList<>();
        //     // for (FakeStoreProductDto dto: response) {
        //     //     products.add(convertFakeStoreProductDtoToProduct(dto));
        //     // }
        //     listOperations.rightPushAll("ProductList", products);
        // }
        List<Product> products = getAllProductsFromFakeStore();
        sortProducts(products, sortBy, sortOrder);
        
        int end = Math.min((start + pageable.getPageSize()), products.size());
        List<Product> productSubList = products.subList(start, end);
        return new PageImpl<>(productSubList, pageable, products.size());
    }

    public List<Product> getAllProductsFromFakeStore() {
        
        FakeStoreProductDto[] response = restTemplate.getForObject("https://fakestoreapi.com/products/", FakeStoreProductDto[].class);
        List<Product> products = new ArrayList<>();
        for (FakeStoreProductDto dto: response) {
            products.add(convertFakeStoreProductDtoToProduct(dto));
        }
        return products;
    }

    private void sortProducts(List<Product> products, String sortBy, String sortOrder) {

        Comparator<Product> comparator;
        switch (sortBy) {
            case "title":
                comparator = Comparator.comparing(Product::getTitle);
                break;
            case "price":
                comparator = Comparator.comparing(Product::getPrice);
                break;
            case "category":
                comparator = Comparator.comparing(product -> product.getCategory().getName());
                break;
            default:
                comparator = Comparator.comparing(Product::getId);
                break;
        }

        if ("desc".equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }

        products.sort(comparator);
    }

    public Page<Product> getProductsInCategory(String category) {
        FakeStoreProductDto[] response = restTemplate.getForObject("https://fakestoreapi.com/products/category/"+category, FakeStoreProductDto[].class);
        List<Product> products = new ArrayList<>();
        for (FakeStoreProductDto dto: response) {
            products.add(convertFakeStoreProductDtoToProduct(dto));
        }
        return new PageImpl<>(products);
    }

    public Product addNewProduct(Product product) {
        FakeStoreProductDto productDto = convertProductToFakeStoreProductDto(product);
        FakeStoreProductDto response = this.restTemplate.postForObject("https://fakestoreapi.com/products", productDto, FakeStoreProductDto.class);
        return convertFakeStoreProductDtoToProduct(response);
    }

    public Product replaceProduct(Long id, Product product) {
        
        FakeStoreProductDto productDto = convertProductToFakeStoreProductDto(product);
        RequestCallback requestCallback = restTemplate.httpEntityCallback(productDto, FakeStoreProductDto.class);
		HttpMessageConverterExtractor<FakeStoreProductDto> responseExtractor =
				new HttpMessageConverterExtractor<>(FakeStoreProductDto.class, restTemplate.getMessageConverters());
		FakeStoreProductDto response = restTemplate.execute("https://fakestoreapi.com/products/"+id, HttpMethod.PUT, requestCallback, responseExtractor);
        return convertFakeStoreProductDtoToProduct(response);
    }

    public Product updateProduct(Long id, Product product) {

        FakeStoreProductDto productDto = convertProductToFakeStoreProductDto(product);
        FakeStoreProductDto response = this.restTemplate.patchForObject("https://fakestoreapi.com/products/"+id, productDto, FakeStoreProductDto.class);
        return convertFakeStoreProductDtoToProduct(response);
    }

    public Product deleteProduct(Long id) {
        RequestCallback requestCallback = restTemplate.httpEntityCallback(null, FakeStoreProductDto.class);
		HttpMessageConverterExtractor<FakeStoreProductDto> responseExtractor =
				new HttpMessageConverterExtractor<>(FakeStoreProductDto.class, restTemplate.getMessageConverters());
        FakeStoreProductDto response = restTemplate.execute("https://fakestoreapi.com/products/"+id, HttpMethod.DELETE, requestCallback, responseExtractor);
        return convertFakeStoreProductDtoToProduct(response);
    }

    private Product convertFakeStoreProductDtoToProduct(FakeStoreProductDto productDto) {
        Product product = new Product();
        product.setTitle(productDto.getTitle());
        product.setId(productDto.getId());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setImageUrl(productDto.getImage());
        product.setCategory(new Category());
        product.getCategory().setName(productDto.getCategory());

        return product;
    }

    private FakeStoreProductDto convertProductToFakeStoreProductDto(Product product) {
        FakeStoreProductDto dto = new FakeStoreProductDto();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setImage(product.getImageUrl());
        dto.setCategory(product.getCategory().getName());
        return dto;
    }
}
