package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.LikeDto;
import com.example.foruminforexchange.dto.UserDto;
import com.example.foruminforexchange.model.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class LikeMapper {

    @Autowired
    private UserMapper userMapper;

    public LikeDto convertToLikeDto(Like like){
        LikeDto likeDto = new LikeDto();
        likeDto.setLikeId(like.getLikeId());
        likeDto.setUserDto(userMapper.convertToUserDto(like.getUser()));
        likeDto.setCommentId(like.getComment().getCommentId());
        likeDto.setCreateAt(like.getCreateAt());
        return likeDto;
    }
}
