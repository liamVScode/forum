package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.mapper.UserMapper;
import com.example.foruminforexchange.model.Role;
import com.example.foruminforexchange.model.User;
import com.example.foruminforexchange.repository.UserRepo;
import com.example.foruminforexchange.service.AuthenticationService;
import com.example.foruminforexchange.service.EmailService;
import com.example.foruminforexchange.service.JWTService;
import com.example.foruminforexchange.service.UserService;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    private final EmailService emailService;

    private final UserService userService;
    public User signup(SignupRequest signupRequest){
        Optional<User> existingUser = userRepo.findByEmail(signupRequest.getEmail());

        // In log giá trị của existingUser để kiểm tra
        System.out.println("findByEmail returned: " + existingUser);
        if(!existingUser.isEmpty()){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        
        User user = new User();

        user.setEmail(signupRequest.getEmail());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        return userRepo.save(user);
    }

    public JwtAuthenticationResponse signin(SigninRequest signinRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));

        var user = userRepo.findByEmail(signinRequest.getEmail()).orElseThrow(() -> new AppException(ErrorCode.EMAIL_PASSWORD_NOT_TRUE));
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String userEmail = jwtService.extractUsername(refreshTokenRequest.getToken());
        User user = userRepo.findByEmail(userEmail).orElseThrow();
        if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }
        return null;
    }

    public JwtAuthenticationResponse facebookSignin(String accessToken) {

        //goi API lay thong tin user
        FacebookUser facebookUser = fetchFacebookUser(accessToken);
        if(facebookUser == null || facebookUser.getEmail() == null){
            throw new IllegalArgumentException("Invalid Facebook accessToken or cannot fetch Facebook User");
        }

        //check user trong database
        User user = userRepo.findByEmail(facebookUser.getEmail())
                .orElseGet(() ->{
                    User newUser = new User();
                    newUser.setEmail(facebookUser.getEmail());
                    newUser.setFirstName(facebookUser.getFirstName());
                    newUser.setLastName(facebookUser.getLastName());
                    return userRepo.save(newUser);
                });

        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }

    private FacebookUser fetchFacebookUser(String accessToken){
        HttpClient httpClient = HttpClient.newHttpClient();

        String uri = "https://graph.facebook.com/me?fields=id,name,email,first_name,last_name&access_token=" + accessToken;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

        try{
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            JSONParser jsonParser = new JSONParser(JSONParser.MODE_PERMISSIVE);
            JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody);

            return UserMapper.convertToFacebookUser(jsonObject);
        } catch (IOException | InterruptedException | net.minidev.json.parser.ParseException e){
            e.printStackTrace();
            return null;
        }
    }

    public JwtAuthenticationResponse googleSignin(String accessToken) {

        //goi API lay thong tin user
        GoogleUser googleUser = fetchGoogleUser(accessToken);
        if(googleUser == null || googleUser.getEmail() == null){
            throw new IllegalArgumentException("Invalid google accessToken or cannot fetch Google User");
        }

        //check user trong database
        User user = userRepo.findByEmail(googleUser.getEmail())
                .orElseGet(() ->{
                    User newUser = new User();
                    newUser.setEmail(googleUser.getEmail());
                    newUser.setFirstName(googleUser.getFirstName());
                    newUser.setLastName(googleUser.getLastName());
                    return userRepo.save(newUser);
                });

        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }

    private GoogleUser fetchGoogleUser(String accessToken){
        HttpClient httpClient = HttpClient.newHttpClient();

        String uri = "https://people.googleapis.com/v1/people/me?personFields=names,emailAddresses&access_token=" + accessToken;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            JSONParser jsonParser = new JSONParser(JSONParser.MODE_PERMISSIVE);
            JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody);

            return UserMapper.convertToGoogleUser(jsonObject);
        } catch (IOException | InterruptedException | net.minidev.json.parser.ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String requestForgetPassword(ForgetPasswordRequest forgetPasswordRequest){
        User user = userRepo.findByEmail(forgetPasswordRequest.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        //tao token reset password
        String token = jwtService.generatePasswordResetToken(user.getEmail());

        //link reset password
        String urlRs = "http://localhost:4200/reset-password?token=" + token;

        //gui toi mail
        emailService.sendEmail(forgetPasswordRequest.getEmail(), "RESET PASSWORD", "CLICK TO CONTINUE: " + urlRs);
        System.out.println(urlRs);
        return "Đã gửi yêu cầu";
    }

    public String requestResetPassword(ResetPasswordRequest resetPasswordRequest){
        if(!jwtService.isTokenResetPasswordValid(resetPasswordRequest.getToken()) ){
            throw new AppException();
        }
        //lay email tu token
        String email = jwtService.extractUsername(resetPasswordRequest.getToken());

        User user = userRepo.findByEmail(email).get();

        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepo.save(user);

        return "Đã thay đổi mật khẩu thành công";
    }
}
