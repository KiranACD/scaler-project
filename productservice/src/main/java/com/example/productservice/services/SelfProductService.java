package com.example.productservice.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.productservice.exceptions.CategoryNotFoundException;
import com.example.productservice.exceptions.ProductNotFoundException;
import com.example.productservice.models.Category;
import com.example.productservice.models.Product;
import com.example.productservice.models.ProductDocument;
import com.example.productservice.repositories.CategoryRepository;
import com.example.productservice.repositories.ProductRepository;
// import com.example.productservice.repositories.ProductSearchRepository;

@Service("selfProductService")
public class SelfProductService implements ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    // private ProductSearchRepository productSearchRepository;
    // private ElasticsearchOperations elasticsearchOperations;
    private FakeStoreProductService fakeStoreProductService;

    // ProductSearchRepository productSearchRepository, 
    // ElasticsearchOperations elasticsearchOperations,

    @Autowired
    public SelfProductService(ProductRepository productRepository, 
                              CategoryRepository categoryRepository, 
                              FakeStoreProductService fakeStoreProductService) {
                                
        
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        // this.productSearchRepository = productSearchRepository;
        // this.elasticsearchOperations = elasticsearchOperations;
        this.fakeStoreProductService = fakeStoreProductService;
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
    public Page<Product> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortOrder) {
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
        ProductDocument productDocument = convertProductToProductDocument(savedProduct);
        // ProductDocument productDocument = new ProductDocument();
        // productDocument.setId(savedProduct.getId());
        // productDocument.setTitle(savedProduct.getTitle());
        // productDocument.setPrice(savedProduct.getPrice());
        // productDocument.setCategory(savedProduct.getCategory().getName());
        // productDocument.setDescription(savedProduct.getDescription());
        // productDocument.setImageUrl(savedProduct.getImageUrl());
        // productSearchRepository.save(productDocument);
        return savedProduct;
    }

    public ProductDocument convertProductToProductDocument(Product product) {
        ProductDocument productDocument = new ProductDocument();
        productDocument.setId(product.getId());
        productDocument.setTitle(product.getTitle());
        productDocument.setPrice(product.getPrice());
        productDocument.setCategory(product.getCategory().getName());
        productDocument.setDescription(product.getDescription());
        productDocument.setImageUrl(product.getImageUrl()); 
        return productDocument;
    }
    
    @Override
    public List<ProductDocument> searchProducts(String searchString) {
        
        // List<String> searchTerms = Arrays.asList(searchString.split(" "));

        // BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        // for (String term: searchTerms) {
        //     boolQueryBuilder.should(new MatchQuery.Builder()
        //             .field("title").query(term)
        //             .build()._toQuery());
        //     boolQueryBuilder.should(new MatchQuery.Builder()
        //             .field("category").query(term)
        //             .build()._toQuery());
        //     boolQueryBuilder.should(new MatchQuery.Builder()
        //             .field("description").query(term)
        //             .build()._toQuery());
        // }

        // NativeQuery searchQuery = NativeQuery.builder()
        //         .withQuery(boolQueryBuilder.build()._toQuery())
        //         .build();

        // SearchHits<ProductDocument> searchProductHits = elasticsearchOperations.search(searchQuery, ProductDocument.class);

        // if (searchProductHits.getTotalHits() == 0) {
        //     return Collections.emptyList();
        // }

        // List<ProductDocument> products = searchProductHits.getSearchHits().stream()
        //                 .map(SearchHit::getContent)
        //                 .collect(Collectors.toList());
        
        // return products;
        return null;
    }

    @Override
    public void seedRepositories() {
        List<Product> savedProducts = seedDB();
        seedElasticsearch();        
        System.out.println("Database seeded with " + savedProducts.size() + " products");
    }

    public List<Product> getProductsFromFakeStore() {
        return fakeStoreProductService.getAllProductsFromFakeStore();
    }

    
    public List<Product> seedDB() {
        List<Product> products = getProductsFromFakeStore();
        List<Product> savedProducts = productRepository.saveAll(products);
        return savedProducts;
    }

    @Override
    public void seedElasticsearch() {
        List<Product> savedProducts = productRepository.findAll();
        if (savedProducts.isEmpty()) {
            savedProducts = getProductsFromFakeStore();
        }
        List<ProductDocument> productDocuments = getProductDocumentsFromProducts(savedProducts);
        // productSearchRepository.saveAll(productDocuments);
    } 

    public List<ProductDocument> getProductDocumentsFromProducts(List<Product> products) {
        List<ProductDocument> productDocuments = products.stream()
                                .map(this::convertProductToProductDocument)
                                .collect(Collectors.toList());
        return productDocuments;
    }

    
}
