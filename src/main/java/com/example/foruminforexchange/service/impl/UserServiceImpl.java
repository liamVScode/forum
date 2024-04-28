package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.configuration.SecurityUtil;
import com.example.foruminforexchange.dto.ActivityDto;
import com.example.foruminforexchange.dto.UserDto;
import com.example.foruminforexchange.mapper.ActivityMapper;
import com.example.foruminforexchange.mapper.UserMapper;
import com.example.foruminforexchange.model.Activity;
import com.example.foruminforexchange.model.Status;
import com.example.foruminforexchange.model.User;
import com.example.foruminforexchange.repository.ActivityRepo;
import com.example.foruminforexchange.repository.UserRepo;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final ActivityRepo activityRepo;

    private final SecurityUtil securityUtil;

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
    public List<ActivityDto> getAllActivityByUserId() {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        User user = userRepo.findUserByEmail(securityUtil.getCurrentUsername());
        List<Activity> lstActivity = activityRepo.findAllByUserUserId(user.getUserId());

        List<ActivityDto> lstActivityDto = lstActivity.stream()
                .map(activity -> ActivityMapper.convertToActivityDto(activity))
                .collect(Collectors.toList());

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

        Page<UserDto> userDtos = users.map(user -> UserMapper.convertToUserDto(user));
        return userDtos;
    }

    public void setUserStatus(String userEmail, Status status){
        if(userEmail == null){
            throw new AppException(ErrorCode.NOT_BLANK);
        }
        User user = userRepo.findUserByEmail(userEmail);
        if(user == null){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        user.setStatus(status);
        userRepo.save(user);
    }

}
