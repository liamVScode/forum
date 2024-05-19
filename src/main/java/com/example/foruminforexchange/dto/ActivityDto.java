package com.example.foruminforexchange.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityDto {
    String type;
    String content;
    Long postId;
    String postContent;
    LocalDateTime createdAt;
    String link;
}
