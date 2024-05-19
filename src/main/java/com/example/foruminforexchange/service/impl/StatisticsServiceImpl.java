package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.dto.UserDto;
import com.example.foruminforexchange.mapper.UserMapper;
import com.example.foruminforexchange.model.Role;
import com.example.foruminforexchange.model.Status;
import com.example.foruminforexchange.model.User;
import com.example.foruminforexchange.repository.CommentRepo;
import com.example.foruminforexchange.repository.LikeRepo;
import com.example.foruminforexchange.repository.PostRepo;
import com.example.foruminforexchange.repository.UserRepo;
import com.example.foruminforexchange.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final UserRepo userRepo;
    private final CommentRepo commentRepo;
    private final PostRepo postRepo;
    private final LikeRepo likeRepo;
    @Override
    public Long getNumberOfOnlineUser() {
        Long numberOfOnlineUser = userRepo.countByStatusAndRole(Status.ONLINE, Role.USER);
        return numberOfOnlineUser;
    }

    @Override
    public List<UserDto> getListOnlineAdmin() {
        List<User> users = userRepo.findAllByRole(Status.ONLINE, Role.ADMIN);
        if(users == null) throw new AppException(ErrorCode.USER_NOT_FOUND);
        List<UserDto> userDtos = users.stream().map(user -> UserMapper.convertToUserDto(user)).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public Long getNumberOfPostPerDay() {
        return null;
    }

    @Override
    public Long getNumberOfPostPerMonth() {
        return null;
    }

    @Override
    public Long getNuumberOfPost() {
        return postRepo.countAll();
    }

    @Override
    public Long getNumberOfComment() {
        return commentRepo.countAll();
    }

    @Override
    public Long getNumberOfMember() {
        Long numberOfMember = userRepo.countAll();
        return numberOfMember;
    }

    @Override
    public Long getNumberOfPostByUser(Long userId) {
        return postRepo.countAllByUserUserId(userId);
    }

    @Override
    public Long getNumberOfCommentByUser(Long userId) {
        return commentRepo.countAllByUserUserId(userId);
    }

    @Override
    public Long getNumberOfLikeByUserId(Long userId) {
        return likeRepo.countAllByUserUserId(userId);
    }
}
