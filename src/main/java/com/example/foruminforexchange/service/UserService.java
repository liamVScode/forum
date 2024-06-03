package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.model.Activity;
import com.example.foruminforexchange.model.Relationship;
import com.example.foruminforexchange.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UserDetailsService userDetailsService();

    Page<ActivityDto> getAllActivityByCurrentUser(Pageable pageable);

    Page<ActivityDto> getAllActivityByUserId(Long userId, Pageable pageable);

    Page<UserDto> getAllUser(Pageable pageable);

    UserDto editProfile(EditProfileRequest editProfileRequest);

    UserDto changeAvatar(MultipartFile avatar);

    UserDto updateStatus(UpdateStatusRequest updateStatusRequest);

    UserDto lockUser(Long userId);

    UserDto unlockUser(Long userId);

    String followUser(Long userId);

    String unfollowUser(Long userId);

    List<RelationshipDto> getAllRelationship();

    UserDto getInformationUser(Long userId);
}
