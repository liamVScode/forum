package com.example.foruminforexchange.controller;

import com.example.foruminforexchange.dto.ApiResponse;
import com.example.foruminforexchange.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticsService statisticsService;

    @GetMapping("online-user")
    public ApiResponse<Long> getNumberOfOnlineUser(){
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticsService.getNumberOfOnlineUser());
        return apiResponse;
    }

    @GetMapping("number-of-post")
    public ApiResponse<Long> getNumberOfPost(){
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticsService.getNuumberOfPost());
        return apiResponse;
    }

    @GetMapping("number-of-comment")
    public ApiResponse<Long> getNumberOfComment(){
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticsService.getNumberOfComment());
        return apiResponse;
    }

    @GetMapping("number-of-user")
    public ApiResponse<Long> getNumberOfUser(){
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticsService.getNumberOfMember());
        return apiResponse;
    }

    @GetMapping("/average-per-day")
    public ApiResponse<Long> getAveragePostsPerDay() {
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticsService.getNumberOfPostPerDay());
        return apiResponse;
    }

    @GetMapping("/average-per-month")
    public ApiResponse<Long> getAveragePostsPerMonth() {
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticsService.getNumberOfPostPerMonth());
        return apiResponse;
    }
}
