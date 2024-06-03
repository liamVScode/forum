package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.configuration.SecurityUtil;
import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.mapper.UserMapper;
import com.example.foruminforexchange.model.Role;
import com.example.foruminforexchange.model.Status;
import com.example.foruminforexchange.model.User;
import com.example.foruminforexchange.repository.UserRepo;
import com.example.foruminforexchange.service.AuthenticationService;
import com.example.foruminforexchange.service.EmailService;
import com.example.foruminforexchange.service.JWTService;
import com.example.foruminforexchange.service.UserService;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    private final EmailService emailService;

    private final UserService userService;

    private final SecurityUtil securityUtil;
    private final UserMapper userMapper;
    public User signup(SignupRequest signupRequest){
        Optional<User> existingUser = userRepo.findByEmail(signupRequest.getEmail());

        System.out.println("findByEmail returned: " + existingUser);
        if(!existingUser.isEmpty()){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        
        User user = new User();

        user.setEmail(signupRequest.getEmail());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setLocation(signupRequest.getLocation());
        user.setAvatar("https://localhost:3000/Image/ecf5d16b-ed81-4ddf-9f8c-346bcbaa65ce.jpg");
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        return userRepo.save(user);
    }

    public JwtAuthenticationResponse signin(SigninRequest signinRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));

        var user = userRepo.findByEmail(signinRequest.getEmail()).orElseThrow(() -> new AppException(ErrorCode.EMAIL_PASSWORD_NOT_TRUE));
        if(user.getLocked() == true){
            throw new AppException(ErrorCode.LOCKED_USER);
        }
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        user.setStatus(Status.ONLINE);
        userRepo.save(user);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        UserDto userDto = userMapper.convertToUserDto(user);

        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        jwtAuthenticationResponse.setUserDto(userDto);
        return jwtAuthenticationResponse;
    }

    public JwtAuthenticationResponse signinAdmin(SigninRequest signinRequest) {
        // Xác thực thông tin đăng nhập
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));

        // Tìm người dùng theo email và kiểm tra có phải là admin
        var user = userRepo.findByEmail(signinRequest.getEmail()).orElseThrow(() -> new AppException(ErrorCode.EMAIL_PASSWORD_NOT_TRUE));
        if (user.getRole() != Role.ADMIN) {
            throw new AppException(ErrorCode.NOT_ADMIN);
        }
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        user.setStatus(Status.ONLINE);
        userRepo.save(user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        UserDto userDto = userMapper.convertToUserDto(user);
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        jwtAuthenticationResponse.setUserDto(userDto);

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

    public JwtAuthenticationResponse facebookSignin(FacebookAccessToken facebookAccessToken) {
        FacebookUser facebookUser = fetchFacebookUser(facebookAccessToken.getAccessToken());
        if (facebookUser == null || facebookUser.getFacebookId() == null) {
            throw new IllegalArgumentException("Invalid Facebook accessToken or cannot fetch Facebook User");
        }

        // check fbId
        User user = userRepo.findByFacebookId(facebookUser.getFacebookId())
                .orElseGet(() -> {
                    // check email
                    Optional<User> userByEmail = userRepo.findByEmail(facebookUser.getEmail());
                    if (userByEmail.isPresent()) {
                        if(userByEmail.get().getLocked() == true){
                            throw new AppException(ErrorCode.LOCKED_USER);
                        }
                        User existingUser = userByEmail.get();
                        existingUser.setFacebookId(facebookUser.getFacebookId());
                        existingUser.setStatus(Status.ONLINE);
                        existingUser.setFacebook("https://www.facebook.com/" + facebookUser.getFacebookId());
                        return userRepo.save(existingUser);
                    } else {
                        User newUser = new User();
                        newUser.setFacebookId(facebookUser.getFacebookId());
                        newUser.setEmail(facebookUser.getEmail());
                        newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                        newUser.setFirstName(facebookUser.getFirstName());
                        newUser.setLastName(facebookUser.getLastName());
                        newUser.setAvatar("https://localhost:3000/Image/ecf5d16b-ed81-4ddf-9f8c-346bcbaa65ce.jpg");
                        newUser.setFacebook("https://www.facebook.com/" + facebookUser.getFacebookId());
                        newUser.setRole(Role.USER);
                        newUser.setStatus(Status.ONLINE);
                        return userRepo.save(newUser);
                    }
                });

        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        jwtAuthenticationResponse.setUserDto(userMapper.convertToUserDto(user));
        return jwtAuthenticationResponse;
    }

    private FacebookUser fetchFacebookUser(String accessToken) {
        HttpClient httpClient = HttpClient.newHttpClient();
        String uri = "https://graph.facebook.com/v19.0/me?fields=id,name,email,first_name,last_name,birthday,location,hometown,gender,picture&access_token=" + accessToken;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            JSONParser jsonParser = new JSONParser(JSONParser.MODE_PERMISSIVE);
            JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody);

            return userMapper.convertToFacebookUser(jsonObject);

        } catch (IOException | InterruptedException | net.minidev.json.parser.ParseException e) {
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

    @Override
    public String changePassword(ChangePasswordRequest changePasswordRequest) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepo.findUserByEmail(currentUserEmail);
        if(user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        if(!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())){
            throw new AppException(ErrorCode.PASSWORD_NOT_TRUE);
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepo.save(user);

        return "Change password successfully!";
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

            return userMapper.convertToGoogleUser(jsonObject);
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
        emailService.sendEmail(forgetPasswordRequest.getEmail(), "LẤY LẠI MẬT KHẨU", "NHẤN VÀO ĐÂY: " + urlRs);
        return "Request is sent";
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

        return "Changed password successfully";
    }

    public String logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            User user = userRepo.findUserByEmail(currentUserEmail);
            String token = authHeader.substring(7);
            jwtService.blacklistToken(token);
            SecurityContextHolder.clearContext();
            user.setStatus(Status.OFFLINE);
            userRepo.save(user);
            return "Logout successfully!";
        }
        return "Hi";
    }
}
