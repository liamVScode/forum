package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.MessageDto;

import java.util.List;

public interface ChatService {

    List<MessageDto> getMessageByChatId(Long chatId);

    Long createAndOrGetChat(Long chatId);

    String generateTimeStamp();
}
