package com.example.userservice.security.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.userservice.models.Role;
import com.example.userservice.models.User;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize
public class CustomUserDetails implements UserDetails {

    private String password;
    private String username;
    private Long userId;
    private List<CustomGrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    
    public CustomUserDetails() {}

    public CustomUserDetails(User user) {
        this.password = user.getHashedPassword();
        this.username = user.getEmail();
        this.userId = user.getId();
        List<CustomGrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role role: user.getRoles()) {
            grantedAuthorities.add(new CustomGrantedAuthority(role));
        }
        this.authorities = grantedAuthorities;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.enabled = true;
        this.credentialsNonExpired = true;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
    
    
}
