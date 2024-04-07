package com.example.foruminforexchange.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentDto {
    Long commentId;
    String content;
    Long postId;
    List<String> imageUrls;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    UserDto user;
}
