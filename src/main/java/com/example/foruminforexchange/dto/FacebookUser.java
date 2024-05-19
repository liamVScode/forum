package com.example.foruminforexchange.dto;

import lombok.Data;

@Data
public class FacebookUser {
    private String facebookId;
    private String email;
    private String firstName;
    private String lastName;
    private String birthday;
    private String location;
    private String hometown;
    private String gender;
}
