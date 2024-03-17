package com.example.foruminforexchange.dto;

import lombok.Data;

@Data
public class GoogleUser {
    private String email;
    private String firstName;
    private String lastName;


    public GoogleUser(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
