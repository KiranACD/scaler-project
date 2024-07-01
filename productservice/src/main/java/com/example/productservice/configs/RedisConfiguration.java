package com.example.productservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.example.productservice.models.Product;

@Configuration
public class RedisConfiguration {
    
    @Bean
    public RedisTemplate<String, Product> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Product> template = new RedisTemplate();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
