package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.model.User;

public interface AuthenticationService {
    User signup(SignupRequest signupRequest);

    JwtAuthenticationResponse signin(SigninRequest signinRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    JwtAuthenticationResponse facebookSignin(String accessToken);

    JwtAuthenticationResponse googleSignin(String accessToken);

    String requestForgetPassword(ForgetPasswordRequest forgetPasswordRequest);

    String requestResetPassword(ResetPasswordRequest resetPasswordRequest);
}
