package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.LikeDto;
import com.example.foruminforexchange.dto.UserDto;
import com.example.foruminforexchange.model.Like;

import java.util.List;

public class LikeMapper {

    public static LikeDto convertToLikeDto(Like like){
        LikeDto likeDto = new LikeDto();
        likeDto.setLikeId(like.getLikeId());
        likeDto.setUserDto(UserMapper.convertToUserDto(like.getUser()));
        likeDto.setCommentId(like.getComment().getCommentId());
        likeDto.setCreateAt(like.getCreateAt());
        return likeDto;
    }
}
