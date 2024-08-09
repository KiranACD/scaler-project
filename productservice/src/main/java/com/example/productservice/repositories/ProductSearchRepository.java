package com.example.productservice.repositories;

import java.util.List;

// import org.elasticsearch.index.query.BoolQueryBuilder;
// import org.elasticsearch.index.query.QueryBuilders;
// import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
// import org.springframework.data.elasticsearch.core.SearchHit;
// import org.springframework.data.elasticsearch.core.SearchHits;
// import org.springframework.data.elasticsearch.core.query.Query;
// import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.productservice.models.ProductDocument;

// @Repository
// public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {
    
//     List<ProductDocument> findByTitle(String title);
//     List<ProductDocument> findByCategory(String category);

//     // default List<ProductDocument> searchByKeywords(ElasticsearchRestTemplate elasticsearchRestTemplate, List<String> keywords) {
//     //     BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//     //     for (String keyword: keywords) {
//     //         boolQuery.should(QueryBuilders.multiMatchQuery(keyword, "title", "description", "category"));
//     //     }

//     //     Query searchQuery = new NativeSearchQueryBuilder().withQuery(boolQuery).build();

//     //     SearchHits<ProductDocument> searchHits = elasticsearchRestTemplate.search(searchQuery, ProductDocument.class);
//     //     return searchHits.map(SearchHit::getContent).toList();
//     // }
// }
