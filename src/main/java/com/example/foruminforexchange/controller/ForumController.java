package com.example.foruminforexchange.controller;

import com.example.foruminforexchange.dto.ApiResponse;
import com.example.foruminforexchange.dto.ForumInforResponse;
import com.example.foruminforexchange.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/forum")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;

    @GetMapping("/forum-info")
    public ApiResponse<List<ForumInforResponse>> getForumInfo(){
        ApiResponse<List<ForumInforResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(forumService.getForumInfor());
        return apiResponse;
    }
}
