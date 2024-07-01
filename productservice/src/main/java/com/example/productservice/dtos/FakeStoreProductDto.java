package com.example.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

// This class is used to send and receive information to and from the fakestoreapi.
// The names of the attributes are what the api expects in the request body and what the response
// json from the api will contain.
@Getter
@Setter
public class FakeStoreProductDto {
    private Long id;
    private String title;
    private double price;
    private String category;
    private String description;
    private String image;
}
