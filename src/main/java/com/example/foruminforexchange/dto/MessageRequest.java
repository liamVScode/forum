package com.example.foruminforexchange.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageRequest {
    private Long chatId;
    private Long userId; //from User
    private String messageContent;
}
