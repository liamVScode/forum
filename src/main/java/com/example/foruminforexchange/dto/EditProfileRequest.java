package com.example.foruminforexchange.dto;

import com.example.foruminforexchange.model.Role;
import com.example.foruminforexchange.model.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EditProfileRequest {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String location;
    private String website;
    private String about;
    private String skype;
    private String facebook;
    private String twitter;
}
