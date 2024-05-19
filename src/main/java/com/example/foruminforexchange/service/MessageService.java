package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.MessageDto;
import com.example.foruminforexchange.dto.MessageRequest;

public interface MessageService {
    MessageDto addNewMessage(MessageRequest messageRequest);
}
