package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.ActivityDto;
import com.example.foruminforexchange.dto.UserDto;
import com.example.foruminforexchange.model.Activity;
import com.example.foruminforexchange.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {

    UserDetailsService userDetailsService();

    List<ActivityDto> getAllActivityByUserId();

    Page<UserDto> getAllUser(Pageable pageable);


}
