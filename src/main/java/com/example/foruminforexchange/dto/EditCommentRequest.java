package com.example.foruminforexchange.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EditCommentRequest {
    Long commentId;
    String content;
    Long postId;
    List<String> imageUrls;
}
