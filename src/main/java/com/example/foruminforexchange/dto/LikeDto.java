package com.example.foruminforexchange.dto;

import com.example.foruminforexchange.model.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LikeDto {
    Long likeId;
    LocalDateTime createAt;
    UserDto userDto;
    Long commentId;
}
