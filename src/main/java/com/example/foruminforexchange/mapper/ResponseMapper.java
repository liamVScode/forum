package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.ResponseDto;
import com.example.foruminforexchange.model.Response;
import com.example.foruminforexchange.model.ResponseUser;

import java.util.List;
import java.util.stream.Collectors;

public class ResponseMapper {
    public static ResponseDto convertToResponseDto(Response response, Double votePercentage){
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResponseId(response.getResponseId());
        responseDto.setResponseContent(response.getResponseContent());
        responseDto.setVoteCount(response.getVoteCount());
        responseDto.setResponseUsers(response.getResponseUsers().stream().map(
                responseUser -> ResponseUserMapper.convertToResponseUserDto(responseUser)).collect(Collectors.toList()
        ));
        responseDto.setVotePercentage(votePercentage);
        return responseDto;
    }
}
