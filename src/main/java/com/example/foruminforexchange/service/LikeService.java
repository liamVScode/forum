package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.LikeDto;

import java.util.List;

public interface LikeService {

    List<LikeDto> getAllLikeByComment(Long commentId);
    String likeComment(Long commentId);

    String unlikeComment(Long commentId);
}
