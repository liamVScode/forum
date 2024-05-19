package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.ChatDto;
import com.example.foruminforexchange.dto.CreateChatRequest;
import com.example.foruminforexchange.dto.MessageDto;

import java.util.List;

public interface ChatService {

    List<MessageDto> getMessageByChatId(Long chatId);

    ChatDto createChat(CreateChatRequest createChatRequest);

    String generateTimeStamp();

    ChatDto getPrivateChatWithUser(Long userId);

    List<ChatDto> getAllChatByCurrentUser();
}
