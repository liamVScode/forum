package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.CommentDto;
import com.example.foruminforexchange.dto.CreateCommentRequest;
import com.example.foruminforexchange.dto.DeleteCommentRequest;
import com.example.foruminforexchange.dto.EditCommentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CommentService {
    Page<CommentDto> getAllCommentByPost(Long postId, Pageable pageable);
    CommentDto createComment(CreateCommentRequest createCommentRequest, List<MultipartFile> imageFiles);

    CommentDto editComment(EditCommentRequest editCommentRequest, List<MultipartFile> imageFiles);

    String deleteComment(Long postId, Long commentId);
}
