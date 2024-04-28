package com.example.foruminforexchange.service;


import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Objects;

public interface JWTService {

    String extractUsername(String token);

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenResetPasswordValid(String token);

    String generateRefreshToken(Map<String, Objects> extraClaims, UserDetails userDetails);

    String generatePasswordResetToken(String userEmail);

//    boolean isTokenBlacklisted(String token);
//
//    void blacklistToken(String token);


}
