package com.example.foruminforexchange.dto;

import lombok.Data;

@Data
public class EditCategoryRequest {
    Long categoryId;
    String categoryName;
    Long topicId;
}
