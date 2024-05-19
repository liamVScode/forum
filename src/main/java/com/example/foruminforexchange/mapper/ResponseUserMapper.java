package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.ResponseDto;
import com.example.foruminforexchange.dto.ResponseUserDto;
import com.example.foruminforexchange.model.ResponseUser;

public class ResponseUserMapper {
    public static ResponseUserDto convertToResponseUserDto(ResponseUser responseUser){
        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setResponseUserId(responseUser.getResponseUserId());
        responseUserDto.setResponseId(responseUser.getResponse().getResponseId());
        responseUserDto.setUserId(responseUser.getUser().getUserId());
        return responseUserDto;
    }
}
