package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.NotificationDto;
import com.example.foruminforexchange.dto.UpdateNotiRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

    List<NotificationDto> getAllNotiByUser(Pageable pageable);
    String updateNotificationStatus(UpdateNotiRequest updateNoti);
}
