package com.example.foruminforexchange.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateCommentRequest {
    String content;
    Long postId;
}
