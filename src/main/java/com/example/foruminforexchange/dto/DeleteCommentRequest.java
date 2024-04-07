package com.example.foruminforexchange.dto;

import lombok.Data;

@Data
public class DeleteCommentRequest {
    Long postId;
    Long commentId;
}
