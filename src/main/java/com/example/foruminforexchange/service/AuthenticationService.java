package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.JwtAuthenticationResponse;
import com.example.foruminforexchange.dto.RefreshTokenRequest;
import com.example.foruminforexchange.dto.SigninRequest;
import com.example.foruminforexchange.dto.SignupRequest;
import com.example.foruminforexchange.model.User;

public interface AuthenticationService {
    User signup(SignupRequest signupRequest);

    JwtAuthenticationResponse signin(SigninRequest signinRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
