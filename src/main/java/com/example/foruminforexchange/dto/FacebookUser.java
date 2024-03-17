package com.example.foruminforexchange.dto;

import lombok.Data;

@Data
public class FacebookUser {
    private String email;
    private String firstName;
    private String lastName;

    public FacebookUser(String email, String firstName, String lastName) {
    }
}
