package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.dto.MessageDto;
import com.example.foruminforexchange.dto.MessageRequest;
import com.example.foruminforexchange.mapper.MessageMapper;
import com.example.foruminforexchange.model.Chat;
import com.example.foruminforexchange.model.Message;
import com.example.foruminforexchange.model.User;
import com.example.foruminforexchange.repository.ChatRepo;
import com.example.foruminforexchange.repository.MessageRepo;
import com.example.foruminforexchange.repository.UserRepo;
import com.example.foruminforexchange.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {
    private MessageRepo messageRepo;
    private ChatRepo chatRepo;
    private UserRepo userRepo;
    @Autowired
    private MessageMapper messageMapper;
    @Override
    public MessageDto addNewMessage(MessageRequest messageRequest) {
        Chat chat = chatRepo.findById(messageRequest.getChatId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        User user = userRepo.findById(messageRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Message message = new Message();
        message.setUser(user);
        message.setMessageContent(messageRequest.getMessageContent());
        message.setCreateAt(LocalDateTime.now());
        message.setChat(chat);
        Message savedMessage = messageRepo.save(message);
        chat.setStatus(1L);
        chat.setReceiverStatus(0L);
        chatRepo.save(chat);

        return messageMapper.mapToMessageDto(savedMessage);
    }

}
