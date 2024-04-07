package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.ActivityDto;
import com.example.foruminforexchange.model.Activity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {

    UserDetailsService userDetailsService();

    List<ActivityDto> getAllActivityByUserId();


}
