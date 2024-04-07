package com.example.foruminforexchange.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private String email;
    private String avatar;
    private String firstName;
    private String lastName;
    private LocalDateTime dateOfBirth;
    private String location;
    private String website;
    private String about;
    private String skype;
    private String facebook;
    private String twitter;
    private String role;
}
