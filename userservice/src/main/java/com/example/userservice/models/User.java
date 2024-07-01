package com.example.userservice.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User extends BaseModel {
    
    private String name;
    private String hashedPassword;
    private String email;
    private Boolean isVerified;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;
}
