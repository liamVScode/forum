package com.example.foruminforexchange.controller;

import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.model.Activity;
import com.example.foruminforexchange.model.User;
import com.example.foruminforexchange.service.SearchService;
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
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final SearchService searchService;

    @GetMapping("")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hi User");
    }

    @GetMapping("/all-activity")
    public ApiResponse<Page<ActivityDto>> getAllActivityByCurrentUser(Pageable pageable){
        ApiResponse<Page<ActivityDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getAllActivityByCurrentUser(pageable));
        return apiResponse;
    }

    @GetMapping("/all-activity-by-user")
    public ApiResponse<Page<ActivityDto>> getAllActivityByUserId(@RequestParam Long userId, Pageable pageable){
        ApiResponse<Page<ActivityDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getAllActivityByUserId(userId, pageable));
        return apiResponse;
    }

    @GetMapping("/all-user")
    public ApiResponse<Page<UserDto>> getAllUser(Pageable pageable){
        ApiResponse<Page<UserDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getAllUser(pageable));
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

    @PostMapping("lock-user")
    public ApiResponse<UserDto> lockUser(@RequestParam Long userId){
        ApiResponse<UserDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.lockUser(userId));
        return apiResponse;
    }

    @PostMapping("unlock-user")
    public ApiResponse<UserDto> unlockUser(@RequestParam Long userId){
        ApiResponse<UserDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.unlockUser(userId));
        return apiResponse;
    }

    @GetMapping("/filter-user")
    public ApiResponse<Page<UserDto>> filterUser(@RequestParam(value = "searchKeyword", required = false) String searchKeyword,
                                                 @RequestParam(value = "locked", required = false) Long locked,
                                                 Pageable pageable){
        ApiResponse<Page<UserDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(searchService.filterUser(searchKeyword, locked, pageable));
        return apiResponse;
    }

    @PostMapping("/follow")
    public ApiResponse<String> followUser(@RequestParam Long userId){
        ApiResponse<String> apiResponse = new ApiResponse();
        apiResponse.setResult(userService.followUser(userId));
        return apiResponse;
    }

    @PostMapping("/unfollow")
    public ApiResponse<String> unfollowUser(@RequestParam Long userId){
        ApiResponse<String> apiResponse = new ApiResponse();
        apiResponse.setResult(userService.unfollowUser(userId));
        return apiResponse;
    }

    @GetMapping("/all-relationship")
    public ApiResponse<List<RelationshipDto>> getAllRelationship(){
        ApiResponse<List<RelationshipDto>> apiResponse = new ApiResponse();
        apiResponse.setResult(userService.getAllRelationship());
        return apiResponse;
    }

    @GetMapping("/information-user")
    public ApiResponse<UserDto> getInformationUser(@RequestParam Long userId){
        ApiResponse<UserDto> apiResponse = new ApiResponse();
        apiResponse.setResult(userService.getInformationUser(userId));
        return apiResponse;
    }

}
