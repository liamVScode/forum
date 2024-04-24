package com.example.foruminforexchange.controller;

import com.example.foruminforexchange.dto.ActivityDto;
import com.example.foruminforexchange.dto.ApiResponse;
import com.example.foruminforexchange.model.Activity;
import com.example.foruminforexchange.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hi User");
    }

    @GetMapping("/all-activity")
    public ApiResponse<List<ActivityDto>> getAllActivityByUserId(){
        ApiResponse<List<ActivityDto>> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userService.getAllActivityByUserId());

        return apiResponse;
    }
}
