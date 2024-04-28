package com.example.foruminforexchange.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDto {
    private Long postId;
    private String title;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private Long shareCount;
    private Long reportCount;
    private UserDto user;
    private PrefixDto prefix;
    private CategoryDto category;
    private Boolean locked;
    private PollDto poll;
    private List<ReportDto> reportDto;
    private List<CommentDto> commentDto;
}
