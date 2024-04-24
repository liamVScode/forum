package com.example.foruminforexchange.dto;

import lombok.Data;

@Data
public class EditPostResponse {
    Long postId;
    String title;
    CategoryDto categoryDto;
}
