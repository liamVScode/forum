package com.example.foruminforexchange.controller.admin;

import com.example.foruminforexchange.dto.ApiResponse;
import com.example.foruminforexchange.dto.UserDto;
import com.example.foruminforexchange.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/statistics")
public class StatisticsAdController {

    private final StatisticsService statisticsService;

    @GetMapping("/number-of-online-user")
    public ApiResponse<Long> getNumberOfOnlineUser(){
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticsService.getNumberOfOnlineUser());
        return apiResponse;
    }

    @GetMapping("/list-online-admin")
    public ApiResponse<List<UserDto>> getListOnlineAdmin(){
        ApiResponse<List<UserDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticsService.getListOnlineAdmin());
        return apiResponse;
    }
}
