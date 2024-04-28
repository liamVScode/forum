package com.example.foruminforexchange.dto;

import lombok.Data;

@Data
public class CreateCategoryRequest {
    String categoryName;
    Long topicId;
}
