package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.configuration.SecurityUtil;
import com.example.foruminforexchange.mapper.NotificationMapper;
import com.example.foruminforexchange.model.*;
import com.example.foruminforexchange.repository.*;
import com.example.foruminforexchange.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShareServiceImpl implements ShareService {
    private final ShareRepo shareRepo;
    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final ActivityRepo activityRepo;
    private final NotificationRepo notificationRepo;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SecurityUtil securityUtil;
    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public String sharePost(Long postId) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepo.findUserByEmail(currentUserEmail);
        if(user == null) throw new AppException(ErrorCode.USER_NOT_FOUND);

        Post post = postRepo.findByPostId(postId);
        if(post == null) throw new AppException(ErrorCode.POST_NOT_FOUND);

        Share share = new Share();
        share.setPost(post);
        share.setShareTime(LocalDateTime.now());
        share.setUser(user);
        shareRepo.save(share);

        Activity activity = new Activity();
        activity.setCreatedAt(share.getShareTime());
        activity.setType("share");
        activity.setContent(String.format(
                "Bạn đã chia sẻ bài viết: '%s'",
                post.getTitle()
        ));
        activity.setLink(String.format("/category/%d/detail-post/%d/page/%d", post.getCategory().getCategoryId(), postId , 1, 10));
        activity.setUser(user);
        activityRepo.save(activity);

        if(!user.getUserId().equals(post.getUser().getUserId())){
            Notification ownerNotification = new Notification();
            ownerNotification.setUser(post.getUser());
            ownerNotification.setCreateAt(LocalDateTime.now());
            ownerNotification.setStatus(0L);
            ownerNotification.setType(0L);
            ownerNotification.setNotificationContent(String.format(
                    "%s %s đã chia sẻ bài viết của bạn: '%s'",
                    user.getFirstName(), user.getLastName(), post.getTitle()
            ));
            ownerNotification.setLink(String.format("/category/%d/detail-post/%d/page/%d", post.getCategory().getCategoryId(), postId , 1, 10));
            if (post.getUser() != null) {
                notificationRepo.save(ownerNotification);
                simpMessagingTemplate.convertAndSendToUser(post.getUser().getUserId().toString(), "/topic/notifications", notificationMapper.convertToNotificationDto(ownerNotification));
            } else {
                throw new AppException(ErrorCode.USER_NOT_FOUND);
            }
        }
        post.setShareCount(post.getShareCount() == null ? 1 : post.getShareCount() + 1);
        postRepo.save(post);
        return "Share successfully";
    }
}
