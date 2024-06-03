package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.NotificationDto;
import com.example.foruminforexchange.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    @Autowired
    public UserMapper userMapper;
    public NotificationDto convertToNotificationDto(Notification notification){
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setNotificationId(notification.getNotificationId());
        notificationDto.setNotificationContent(notification.getNotificationContent());
        notificationDto.setUserDto(userMapper.convertToUserDto(notification.getUser()));
        notificationDto.setType(notification.getType());
        notificationDto.setStatus(notification.getStatus());
        notificationDto.setCreateAt(notification.getCreateAt());
        notificationDto.setLink(notification.getLink());
        return notificationDto;
    }
}
