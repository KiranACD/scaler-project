package com.example.emailservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailEventDTO {
    
    private String to;
    private String from;
    private String subject;
    private String body;
}
