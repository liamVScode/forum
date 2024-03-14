package com.example.foruminforexchange.dto;

import lombok.Data;

@Data
public class SignupRequest {
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
}
