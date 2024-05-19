package com.example.foruminforexchange.dto;

import lombok.Data;

@Data
public class UpdateStatusRequest {
    private String email;
    private Long status;
}
