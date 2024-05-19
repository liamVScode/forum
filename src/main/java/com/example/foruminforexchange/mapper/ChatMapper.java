package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.ChatDto;
import com.example.foruminforexchange.model.Chat;

import java.util.stream.Collectors;

public class ChatMapper {
    public static ChatDto convertToChatDto(Chat chat){
        ChatDto chatDto = new ChatDto();
        chatDto.setChatId(chat.getChatId());
        chatDto.setChatName(chat.getChatName());
        chatDto.setChatType(chat.getChatType());
        if(chat.getMessages()!= null)
            chatDto.setMessageDtos(chat.getMessages().stream().map(mes -> MessageMapper.mapToMessageDto(mes)).collect(Collectors.toList()));
        chatDto.setUserDtos(chat.getMembers().stream().map(cha -> UserMapper.convertToUserDto(cha)).collect(Collectors.toList()));
        return chatDto;
    }
}
