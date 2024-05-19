package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.ActivityDto;
import com.example.foruminforexchange.dto.EditProfileRequest;
import com.example.foruminforexchange.dto.UpdateStatusRequest;
import com.example.foruminforexchange.dto.UserDto;
import com.example.foruminforexchange.model.Activity;
import com.example.foruminforexchange.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UserDetailsService userDetailsService();

    Page<ActivityDto> getAllActivityByUserId(Pageable pageable);

    Page<UserDto> getAllUser(Pageable pageable);

    UserDto editProfile(EditProfileRequest editProfileRequest);

    UserDto changeAvatar(MultipartFile avatar);

    UserDto updateStatus(UpdateStatusRequest updateStatusRequest);
}
