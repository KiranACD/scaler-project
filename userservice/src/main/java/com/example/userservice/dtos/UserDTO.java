package com.example.userservice.dtos;

import java.util.List;

import com.example.userservice.models.Role;
import com.example.userservice.models.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String email;
    private String name;
    private List<Role> roles;
    private Boolean isVerified;

    public static UserDTO from(User user) {
        UserDTO userDTO = new UserDTO();
        if (user == null) {
            return null;
        }
        
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setRoles(user.getRoles());
        userDTO.setIsVerified(user.getIsVerified());
        return userDTO;
    }
}
