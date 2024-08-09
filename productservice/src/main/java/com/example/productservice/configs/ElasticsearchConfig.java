package com.example.productservice.configs;

// import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
// import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
// import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

// @Configuration
// @EnableElasticsearchRepositories(basePackages = "com.example.productservice.repositories")
// public class ElasticsearchConfig extends ElasticsearchConfiguration {
    
//     // @Bean
//     // @Override
//     // public RestHighLevelClient elasticsearchClient() {
//     //     ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//     //         .connectedTo("localhost:9200")
//     //         .build();
        
//     //     return RestClients.create(clientConfiguration)
//     //         .rest();
//     // }

//     // @Bean
//     // public ElasticsearchRestTemplate elasticsearchRestTemplate() {
//     //     return new ElasticsearchRestTemplate(elasticsearchClient());
//     // }

//     @Override
//     public ClientConfiguration clientConfiguration() {
//         // TODO Auto-generated method stub
//         return ClientConfiguration.builder().connectedTo("localhost:9200").build();
//     }

// }

    


