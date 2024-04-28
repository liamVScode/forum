package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.UserDto;

import java.util.List;

public interface StatisticsService {
    Long getNumberOfOnlineUser();
    List<UserDto> getListOnlineAdmin();
    Long getNumberOfPostPerDay();
    Long getNumberOfPostPerMonth();

}
