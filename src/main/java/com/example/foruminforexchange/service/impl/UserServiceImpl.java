package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.configuration.SecurityUtil;
import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.mapper.ActivityMapper;
import com.example.foruminforexchange.mapper.RelationshipMapper;
import com.example.foruminforexchange.mapper.UserMapper;
import com.example.foruminforexchange.model.Activity;
import com.example.foruminforexchange.model.Relationship;
import com.example.foruminforexchange.model.Status;
import com.example.foruminforexchange.model.User;
import com.example.foruminforexchange.repository.ActivityRepo;
import com.example.foruminforexchange.repository.RelationshipRepo;
import com.example.foruminforexchange.repository.UserRepo;
import com.example.foruminforexchange.service.FileStorageService;
import com.example.foruminforexchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final ActivityRepo activityRepo;

    private final SecurityUtil securityUtil;

    private final FileStorageService fileStorageService;
    private final UserMapper userMapper;
    private final RelationshipMapper relationshipMapper;
    private final RelationshipRepo relationshipRepo;

    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepo.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
            }
        };
    }



    @Override
    public Page<ActivityDto> getAllActivityByCurrentUser(Pageable pageable) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        if (pageable == null || pageable.getPageSize() <= 0) {
            pageable = PageRequest.of(0, 10, Sort.by("activityId").descending());
        } else {
            int pageSize = Math.min(pageable.getPageSize(), 10);
            pageable = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort().and(Sort.by("activityId").descending()));
        }

        User user = userRepo.findUserByEmail(securityUtil.getCurrentUsername());

        Page<Activity> lstActivity = activityRepo.findAllByUserUserId(user.getUserId(), pageable);

        Page<ActivityDto> lstActivityDto = lstActivity.map(activity -> ActivityMapper.convertToActivityDto(activity));

        return lstActivityDto;
    }

    @Override
    public Page<ActivityDto> getAllActivityByUserId(Long userId, Pageable pageable) {
        if (pageable == null || pageable.getPageSize() <= 0) {
            pageable = PageRequest.of(0, 10, Sort.by("activityId").descending());
        } else {
            int pageSize = Math.min(pageable.getPageSize(), 10);
            pageable = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort().and(Sort.by("activityId").descending()));
        }

        User user = userRepo.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Page<Activity> lstActivity = activityRepo.findAllByUserUserId(user.getUserId(), pageable);
        Page<ActivityDto> lstActivityDto = lstActivity.map(activity -> ActivityMapper.convertToActivityDto(activity));
        return lstActivityDto;
    }

    @Override
    public Page<UserDto> getAllUser(Pageable pageable) {
        if (pageable == null || pageable.getPageSize() <= 0) {
            pageable = PageRequest.of(0, 10, Sort.by("userId").descending());
        } else {
            int pageSize = Math.min(pageable.getPageSize(), 10);
            pageable = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort().and(Sort.by("userId").descending()));
        }

        Page<User> users = userRepo.findAll(pageable);
        if(users == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        Page<UserDto> userDtos = users.map(user -> userMapper.convertToUserDto(user));
        return userDtos;
    }



    @Override
    public UserDto editProfile(EditProfileRequest editProfileRequest) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepo.findUserByEmail(currentUserEmail);
        if(user == null){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        user.setFirstName(editProfileRequest.getFirstName());
        user.setLastName(editProfileRequest.getLastName());
        user.setDateOfBirth(editProfileRequest.getDateOfBirth());
        user.setLocation(editProfileRequest.getLocation());
        user.setWebsite(editProfileRequest.getWebsite());
        user.setAbout(editProfileRequest.getAbout());
        user.setSkype(editProfileRequest.getSkype());
        user.setFacebook(editProfileRequest.getFacebook());
        user.setTwitter(editProfileRequest.getTwitter());
        userRepo.save(user);

        return userMapper.convertToUserDto(user);
    }

    @Override
    public UserDto changeAvatar(MultipartFile avatar) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        User user = userRepo.findUserByEmail(currentUserEmail);
        if(user == null){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        if(avatar != null){
            String imageUrl = fileStorageService.storeFile(avatar);
            user.setAvatar(imageUrl);
            userRepo.save(user);
        }
        return userMapper.convertToUserDto(user);
    }

        public UserDto updateStatus(UpdateStatusRequest updateStatusRequest){
        User user = userRepo.findUserByEmail(updateStatusRequest.getEmail());
        if(user == null){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if(updateStatusRequest.getStatus() == 1)
            user.setStatus(Status.OFFLINE);
        else user.setStatus(Status.ONLINE);

        userRepo.save(user);
        return userMapper.convertToUserDto(user);
    }

    @Override
    public UserDto lockUser(Long userId) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User admin = userRepo.findUserByEmail(currentUserEmail);
        if(admin == null){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepo.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setLocked(true);
        user.setLockedBy(admin);
        userRepo.save(user);
        return userMapper.convertToUserDto(user);
    }

    @Override
    public UserDto unlockUser(Long userId) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User admin = userRepo.findUserByEmail(currentUserEmail);
        if(admin == null){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepo.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setLocked(false);
        user.setLockedBy(null);
        userRepo.save(user);
        return userMapper.convertToUserDto(user);
    }

    @Override
    public String followUser(Long userId) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user_source = userRepo.findUserByEmail(currentUserEmail);
        if(user_source == null){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if(user_source.getUserId() == userId) throw new AppException(ErrorCode.USER_EXISTED);
        User user_target = userRepo.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Relationship relationship = new Relationship();
        relationship.setRelationshipName("follow");
        relationship.setSourceUser(user_source);
        relationship.setTargetUser(user_target);
        relationshipRepo.save(relationship);
        return "Follow successfully!";
    }

    @Override
    public String unfollowUser(Long userId) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user_source = userRepo.findUserByEmail(currentUserEmail);
        if(user_source == null){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user_target = userRepo.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Relationship relationship = relationshipRepo.findBySourceUserAndTargetUser(user_source, user_target);
        if(relationship == null) throw new AppException(ErrorCode.NOT_FOUND);
        relationshipRepo.delete(relationship);
        return "Unfollow successfully!";
    }

    @Override
    public List<RelationshipDto> getAllRelationship() {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepo.findUserByEmail(currentUserEmail);
        if(user == null){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        List<Relationship> relationships = relationshipRepo.findAllBySourceUser(user);
        List<RelationshipDto> relationshipDtos = relationships.stream().map(relationship -> relationshipMapper.convertToRelationshipDto(relationship)).collect(Collectors.toList());
        return relationshipDtos;
    }

    @Override
    public UserDto getInformationUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.convertToUserDto(user);
    }


}
