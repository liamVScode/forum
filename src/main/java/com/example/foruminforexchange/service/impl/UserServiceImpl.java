package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.configuration.SecurityUtil;
import com.example.foruminforexchange.dto.ActivityDto;
import com.example.foruminforexchange.mapper.ActivityMapper;
import com.example.foruminforexchange.model.Activity;
import com.example.foruminforexchange.model.User;
import com.example.foruminforexchange.repository.ActivityRepo;
import com.example.foruminforexchange.repository.UserRepo;
import com.example.foruminforexchange.service.UserService;
import lombok.RequiredArgsConstructor;
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
}
