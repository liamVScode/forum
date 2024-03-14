package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.MessageDto;

public interface MessageService {
    MessageDto addNewMessage(MessageDto messageDto);
}
