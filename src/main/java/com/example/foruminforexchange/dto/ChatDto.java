package com.example.foruminforexchange.dto;

import com.example.foruminforexchange.model.ChatType;
import lombok.Data;

import java.util.List;

@Data
public class ChatDto {
    Long chatId;
    String chatName;
    ChatType chatType;
    Long status;
    Long receiverStatus;
    List<MessageDto> messageDtos;
    List<UserDto> userDtos;
}
