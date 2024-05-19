package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.model.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signinAdmin(SigninRequest signinRequest);
    User signup(SignupRequest signupRequest);
    JwtAuthenticationResponse signin(SigninRequest signinRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    JwtAuthenticationResponse facebookSignin(FacebookAccessToken facebookAccessToken);
    JwtAuthenticationResponse googleSignin(String accessToken);
    String changePassword(ChangePasswordRequest changePasswordRequest);
    String requestForgetPassword(ForgetPasswordRequest forgetPasswordRequest);
    String requestResetPassword(ResetPasswordRequest resetPasswordRequest);
    String logout(HttpServletRequest request);
}
