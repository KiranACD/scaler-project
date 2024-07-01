package com.example.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionDto {

    private String message;
    // Details can have information on what the client can do to avoid exceptions
    private String details;
}