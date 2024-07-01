package com.example.userservice.security.models;

import org.springframework.security.core.GrantedAuthority;

import com.example.userservice.models.Role;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize
public class CustomGrantedAuthority implements GrantedAuthority {

    private String authority;

    public CustomGrantedAuthority() {}

    public CustomGrantedAuthority(Role role) {
        this.authority = role.getName();
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
    
}
