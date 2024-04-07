package com.example.foruminforexchange.dto;

import lombok.Data;

@Data
public class ActivityDto {
    String type;
    String content;
    Long postId;
    String postContent;
}
