package com.example.foruminforexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long messageId;
    private Long chatId;
    private UserDto userDto; //from User
    private String messageContent;
    private LocalDateTime createAt;
}
