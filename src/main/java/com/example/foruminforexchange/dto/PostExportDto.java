package com.example.foruminforexchange.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostExportDto {
    private Long postId;
    private String title;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private Long shareCount;
    private Long reportCount;
    private Long userId;
    private Long prefixId;
    private Long categoryId;
    private Boolean locked;
    private Long pollId;
}
