package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.dto.MessageDto;
import com.example.foruminforexchange.mapper.MessageMapper;
import com.example.foruminforexchange.model.Message;
import com.example.foruminforexchange.repository.MessageRepo;
import com.example.foruminforexchange.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {
    private MessageRepo messageRepo;
    @Override
    public MessageDto addNewMessage(MessageDto messageDto) {
        Message message = MessageMapper.mapToMessage(messageDto);
        Message savedMessage = messageRepo.save(message);
        return MessageMapper.mapToMessageDto(savedMessage);
    }
}
