package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.NotificationDto;
import com.example.foruminforexchange.model.Notification;

public class NotificationMapper {
    public static NotificationDto convertToNotificationDto(Notification notification){
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setNotificationId(notification.getNotificationId());
        notificationDto.setNotificationContent(notification.getNotificationContent());
        notificationDto.setUserDto(UserMapper.convertToUserDto(notification.getUser()));
        notificationDto.setType(notification.getType());
        notificationDto.setStatus(notification.getStatus());
        notificationDto.setCreateAt(notification.getCreateAt());
        notificationDto.setLink(notification.getLink());
        return notificationDto;
    }
}
