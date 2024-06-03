package com.example.foruminforexchange.controller;

import com.example.foruminforexchange.dto.ApiResponse;
import com.example.foruminforexchange.dto.NotificationDto;
import com.example.foruminforexchange.dto.TopicPrefixResponse;
import com.example.foruminforexchange.dto.UpdateNotiRequest;
import com.example.foruminforexchange.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/get-all")
    public ApiResponse<List<NotificationDto>> getAllNotiByUser(Pageable pageable){
        ApiResponse<List<NotificationDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(notificationService.getAllNotiByUser(pageable));
        return apiResponse;
    }

    @PutMapping("/update-notification")
    public ApiResponse<String> updateNotificationStatus(@RequestBody UpdateNotiRequest updateNotiRequest){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(notificationService.updateNotificationStatus(updateNotiRequest));
        return apiResponse;
    }
}
