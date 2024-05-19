package com.example.foruminforexchange.controller;

import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.model.User;
import com.example.foruminforexchange.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "https://localhost:4200"})
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signinAdmin")
    public ApiResponse<JwtAuthenticationResponse> siginAdmin(@RequestBody SigninRequest signinRequest){
        ApiResponse<JwtAuthenticationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(authenticationService.signinAdmin(signinRequest));
        return apiResponse;
    }

    @PostMapping("/signup")
    public ApiResponse<User> signup(@RequestBody SignupRequest signupRequest){
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(authenticationService.signup(signupRequest));
        return apiResponse;
    }

    @PostMapping("/signin")
    public ApiResponse<JwtAuthenticationResponse> signin(@RequestBody SigninRequest signinRequest){
        ApiResponse<JwtAuthenticationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(authenticationService.signin(signinRequest));
        return apiResponse;
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/facebook/signin")
    public ApiResponse<JwtAuthenticationResponse> signinWithFacebook(@RequestBody FacebookAccessToken facebookAccessToken){
        ApiResponse<JwtAuthenticationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(authenticationService.facebookSignin(facebookAccessToken));
        return apiResponse;
    }

    @PostMapping("/google/signin")
    public ApiResponse<JwtAuthenticationResponse> siginWithGoogle(@RequestBody String accessToken){
        ApiResponse<JwtAuthenticationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(authenticationService.googleSignin(accessToken));
        return apiResponse;
    }

    @PostMapping("/change-password")
    public ApiResponse<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(authenticationService.changePassword(changePasswordRequest));
        return apiResponse;
    }

    @PostMapping("/forget-password")
    public ApiResponse<?> requestResetPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(authenticationService.requestForgetPassword(forgetPasswordRequest));
        return apiResponse;
    }

    @PostMapping("/reset-password")
    public ApiResponse<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(authenticationService.requestResetPassword(resetPasswordRequest));
        return apiResponse;
    }

    @PostMapping("logout")
    public ApiResponse<String> logout(HttpServletRequest request){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(authenticationService.logout(request));
        return apiResponse;
    }


}
