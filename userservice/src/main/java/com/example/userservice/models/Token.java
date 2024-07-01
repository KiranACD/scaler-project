package com.example.userservice.models;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Token extends BaseModel {
    private String value;
    @ManyToOne
    private User user;
    private Date expiresAt;
}
