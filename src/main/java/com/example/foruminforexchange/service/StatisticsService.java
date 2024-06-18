package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.UserDto;

import java.util.List;

public interface StatisticsService {
    Long getNumberOfOnlineUser();
    List<UserDto> getListOnlineAdmin();
    Long getNumberOfPostPerDay();
    Long getNumberOfPostPerMonth();

    Long getNumberOfPost();

    Long getNumberOfComment();

    Long getNumberOfMember();

    Long getNumberOfPostByUser(Long userId);

    Long getNumberOfCommentByUser(Long userId);

    Long getNumberOfLikeByUserId(Long userId);

}
