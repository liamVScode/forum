package com.example.foruminforexchange.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateChatRequest {
    String chatName;
    Long chatType;
    List<Long> userId;
}
