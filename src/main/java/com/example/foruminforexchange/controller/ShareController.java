package com.example.foruminforexchange.controller;

import com.example.foruminforexchange.dto.ApiResponse;
import com.example.foruminforexchange.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/share")
@RequiredArgsConstructor
public class ShareController {

    private final ShareService shareService;

    @PostMapping("/share-post")
    public ApiResponse<String> sharePost(@RequestParam Long postId){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(shareService.sharePost(postId));
        return apiResponse;
    }
}
