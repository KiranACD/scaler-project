package com.example.productservice.models;

import java.io.Serializable;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Category extends BaseModel implements Serializable {
    private String name;
}
