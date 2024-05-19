package com.example.foruminforexchange.controller;

import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.model.Activity;
import com.example.foruminforexchange.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "https://localhost:4200"})
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hi User");
    }

    @GetMapping("/all-activity")
    public ApiResponse<Page<ActivityDto>> getAllActivityByUserId(Pageable pageable){
        ApiResponse<Page<ActivityDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getAllActivityByUserId(pageable));
        return apiResponse;
    }

    @PostMapping("edit-profile")
    public ApiResponse<UserDto> editProfile(@RequestBody EditProfileRequest editProfileRequest){
        ApiResponse<UserDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.editProfile(editProfileRequest));
        return apiResponse;
    }

    @PostMapping("/change-avatar")
    public ApiResponse<UserDto> changeAvatar(@RequestParam(value = "avatar", required = true) MultipartFile avatar){
        ApiResponse<UserDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.changeAvatar(avatar));
        return apiResponse;
    }

    @PostMapping("/update-status")
    public ApiResponse<UserDto> updateStatus(@RequestBody UpdateStatusRequest updateStatusRequest){
        ApiResponse<UserDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.updateStatus(updateStatusRequest));
        return apiResponse;
    }
}
