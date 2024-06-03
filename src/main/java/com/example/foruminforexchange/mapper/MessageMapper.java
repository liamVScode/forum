package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.MessageDto;
import com.example.foruminforexchange.dto.UserDto;
import com.example.foruminforexchange.model.Message;
import com.example.foruminforexchange.repository.ChatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {
    @Autowired
    private UserMapper userMapper;
    public MessageDto mapToMessageDto(Message message){
        if(message == null){
            return null;
        }
        return new MessageDto(
            message.getMessageId(),
            message.getChat() != null ? message.getChat().getChatId() : null,
            message.getUser() != null ? userMapper.convertToUserDto(message.getUser()) : null,
            message.getMessageContent(),
            message.getCreateAt()
        );

    }

    public static Message mapToMessage(MessageDto messageDto){
        if(messageDto == null){
            return null;
        }
        Message message = new Message();
        message.setMessageId(messageDto.getMessageId()); // Be cautious with setting ID directly, usually managed by DB
        // Note: For chat and user, you might need to fetch them from the database by their IDs
        // message.setChat(chatRepository.findById(messageDTO.getChatId()).orElse(null));
        // message.setUser(userRepository.findById(messageDTO.getUserId()).orElse(null));
        message.setMessageContent(messageDto.getMessageContent());
        message.setCreateAt(messageDto.getCreateAt());
        return message;
    }

}
