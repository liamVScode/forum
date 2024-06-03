package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.UserDto;
import com.example.foruminforexchange.model.Like;
import com.example.foruminforexchange.model.User;
import com.example.foruminforexchange.repository.CommentRepo;
import com.example.foruminforexchange.repository.LikeRepo;
import com.example.foruminforexchange.repository.PostRepo;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserStatisticMapper {
    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private LikeRepo likeRepo;

    @Autowired
    private PostRepo postRepo;

    public UserDto addStatisticsToDto(UserDto userDto, User user) {
        userDto.setNumberOfComment(commentRepo.countAllByUserUserId(user.getUserId()));
        userDto.setNumberOfLike(likeRepo.countAllByUserUserId(user.getUserId()));
        userDto.setNumberOfPost(postRepo.countAllByCategoryCategoryId(user.getUserId()));
        return userDto;
    }
}
