package com.example.productservice.commons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.productservice.dtos.UserDTO;

@Service
public class AuthenticationCommons {

    private RestTemplate restTemplate;

    @Autowired
    public AuthenticationCommons(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public UserDTO validateToken(String token) {
        ResponseEntity<UserDTO> userDtoResponse = restTemplate.postForEntity(
            "http://localhost:8181/users/validate/" + token, 
            null, 
            UserDTO.class
        );

        UserDTO userDto = userDtoResponse.getBody();
        
        return userDto;
    }
}
