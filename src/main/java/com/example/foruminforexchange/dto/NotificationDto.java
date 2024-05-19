package com.example.foruminforexchange.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDto {
    Long notificationId;
    String notificationContent;
    Long type;
    Long status;
    LocalDateTime createAt;
    UserDto userDto;
    String link;
}
