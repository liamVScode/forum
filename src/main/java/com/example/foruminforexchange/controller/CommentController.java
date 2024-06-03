package com.example.foruminforexchange.controller;


import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.model.Comment;
import com.example.foruminforexchange.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts/comments/")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("comment-by-post")
    public ApiResponse<Page<CommentDto>> getCommentByPost(@RequestParam("postId") Long postId, Pageable pageable){
        ApiResponse<Page<CommentDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(commentService.getAllCommentByPost(postId, pageable));
        return apiResponse;
    }


    @PostMapping("/create-comment")
    public ApiResponse<CommentDto> createComment(@ModelAttribute CreateCommentRequest createCommentRequest, @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles){
        ApiResponse<CommentDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(commentService.createComment(createCommentRequest, imageFiles));
        return apiResponse;
    }

    @PutMapping("/edit-comment")
    public ApiResponse<CommentDto> createComment(@ModelAttribute EditCommentRequest editCommentRequest, @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles) {
        ApiResponse<CommentDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(commentService.editComment(editCommentRequest, imageFiles));
        return apiResponse;
    }

    @DeleteMapping("/delete-comment")
    public ApiResponse<String> deleteComment(@RequestParam("postId") Long postId, @RequestParam("commentId") Long commentId) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(commentService.deleteComment(postId, commentId));
        return apiResponse;
    }
}
