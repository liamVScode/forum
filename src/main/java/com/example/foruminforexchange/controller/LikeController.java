package com.example.foruminforexchange.controller;

import com.example.foruminforexchange.dto.ApiResponse;
import com.example.foruminforexchange.dto.LikeDto;
import com.example.foruminforexchange.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/comments/likes")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "https://localhost:4200"})
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/like-by-comment")
    public ApiResponse<List<LikeDto>> allLikeByComment(@RequestParam("commentId") Long commentId){
        ApiResponse<List<LikeDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(likeService.getAllLikeByComment(commentId));
        return apiResponse;
    }

    @PostMapping("/like-comment")
    public ApiResponse<String> likeComment(@RequestParam("commentId") Long commentId){
        System.out.println(commentId);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(likeService.likeComment(commentId));
        return apiResponse;
    }

    @PostMapping("/unlike-comment")
    public ApiResponse<String> unlikePost(@RequestParam("commentId") Long commentId){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(likeService.unlikeComment(commentId));
        return apiResponse;
    }
}
