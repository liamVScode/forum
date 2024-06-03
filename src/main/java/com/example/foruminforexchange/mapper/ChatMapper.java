package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.ChatDto;
import com.example.foruminforexchange.dto.UserDto;
import com.example.foruminforexchange.model.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatMapper {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MessageMapper messageMapper;
    public ChatDto convertToChatDto(Chat chat){
        ChatDto chatDto = new ChatDto();
        chatDto.setChatId(chat.getChatId());
        chatDto.setChatName(chat.getChatName());
        chatDto.setChatType(chat.getChatType());
        if(chat.getMessages()!= null)
            chatDto.setMessageDtos(chat.getMessages().stream().map(mes -> messageMapper.mapToMessageDto(mes)).collect(Collectors.toList()));
        List<UserDto> userDtos = chat.getMembers().stream()
                .map(userChat -> userMapper.convertToUserDto(userChat.getUser()))
                .collect(Collectors.toList());
        chatDto.setUserDtos(userDtos);
        return chatDto;
    }
}
