package com.example.productservice.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String email;
    private String name;
    private List<Role> roles;
    private Boolean isVerified;

    
}
