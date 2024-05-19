package com.example.foruminforexchange.dto;

import com.example.foruminforexchange.model.ResponseUser;
import lombok.Data;

import java.util.List;

@Data
public class ResponseDto {
    Long responseId;
    String responseContent;
    Long voteCount;
    Double votePercentage = 0.0;
    List<ResponseUserDto> responseUsers;
}
