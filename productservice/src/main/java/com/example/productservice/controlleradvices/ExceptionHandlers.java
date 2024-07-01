package com.example.productservice.controlleradvices;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;

import com.example.productservice.dtos.ExceptionDto;
import com.example.productservice.exceptions.ProductNotFoundException;

@ControllerAdvice
public class ExceptionHandlers {
    
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleProductNotFoundException(ProductNotFoundException exception) {
        
        ExceptionDto dto = new ExceptionDto();
        dto.setMessage(exception.getMessage());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
