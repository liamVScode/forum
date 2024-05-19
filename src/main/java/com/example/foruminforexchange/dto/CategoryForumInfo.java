package com.example.foruminforexchange.dto;

import lombok.Data;

@Data
public class CategoryForumInfo {
    CategoryDto categoryDto;
    Long numberOfPost;
    Long numberOfComment;
    PostDto postDto;
}
