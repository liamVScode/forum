package com.example.foruminforexchange.dto;

import com.example.foruminforexchange.model.Role;
import com.example.foruminforexchange.model.Status;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class UserExportDto {
    private Long userId;
    private String email;
    private String avatar;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String location;
    private Status status;
    private Boolean isLocked;
    private String website;
    private String about;
    private String skype;
    private String facebook;
    private String twitter;
    private Role role;
    private Long lockedBy;
    private LocalDateTime createAt;
    private Long numberOfComment;
    private Long numberOfLike;
    private Long numberOfPost;
}
