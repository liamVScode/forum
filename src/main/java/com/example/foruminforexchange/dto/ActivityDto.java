package com.example.foruminforexchange.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityDto {
    String type;
    String content;
    LocalDateTime createdAt;
    String link;
}
