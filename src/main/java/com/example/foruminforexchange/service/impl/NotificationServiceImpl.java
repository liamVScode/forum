package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.configuration.SecurityUtil;
import com.example.foruminforexchange.dto.NotificationDto;
import com.example.foruminforexchange.dto.UpdateNotiRequest;
import com.example.foruminforexchange.mapper.NotificationMapper;
import com.example.foruminforexchange.model.Notification;
import com.example.foruminforexchange.model.User;
import com.example.foruminforexchange.repository.NotificationRepo;
import com.example.foruminforexchange.repository.UserRepo;
import com.example.foruminforexchange.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepo notificationRepo;

    private final UserRepo userRepo;
    private final SecurityUtil securityUtil;

    @Override
    public List<NotificationDto> getAllNotiByUser(Pageable pageable) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        User user = userRepo.findUserByEmail(currentUserEmail);
        pageable = PageRequest.of(0, 20, Sort.by("createAt").descending());
        List<Notification> notifications = notificationRepo.findAllByUserUserId(user.getUserId(), pageable);
        List<NotificationDto> notificationDtos = notifications.stream().map(notification -> NotificationMapper.convertToNotificationDto(notification)).collect(Collectors.toList());
        return notificationDtos;
    }

    @Override
    public String updateNotificationStatus(UpdateNotiRequest updateNoti) {
        if(updateNoti == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        Notification notification = notificationRepo.findById(updateNoti.getNotificationId()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        notification.setStatus(1L);
        notificationRepo.save(notification);
        return "Update successfully!";
    }
}
