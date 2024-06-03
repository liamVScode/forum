package com.example.foruminforexchange.controller.admin;

import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.service.AuthenticationService;
import com.example.foruminforexchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hi Admin");
    }

    @GetMapping("/user/all-user")
    public ApiResponse<Page<UserDto>> getAllUser(Pageable pageable){
        ApiResponse<Page<UserDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getAllUser(pageable));
        return apiResponse;
    }

}
