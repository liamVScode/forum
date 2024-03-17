package com.example.foruminforexchange.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequest {
        @NotBlank(message = "First Name cannot be blank")
        private String firstName;
        @NotBlank(message = "First Name cannot be blank")
        private String lastName;
        @Email(message = "Email must be a valid email address")
        @NotBlank(message = "Email cannot be blank")
        private String email;
        @NotBlank(message = "Password cannot be blank")
        private String password;
        private String location;
}
