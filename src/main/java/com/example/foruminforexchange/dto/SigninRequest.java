package com.example.foruminforexchange.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SigninRequest {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be a valid email address")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 40, message = "Password must be from 4 to 40 character")
    private String password;
}
