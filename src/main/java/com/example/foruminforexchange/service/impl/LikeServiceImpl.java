package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.configuration.SecurityUtil;
import com.example.foruminforexchange.dto.LikeDto;
import com.example.foruminforexchange.mapper.LikeMapper;
import com.example.foruminforexchange.mapper.NotificationMapper;
import com.example.foruminforexchange.model.*;
import com.example.foruminforexchange.repository.*;
import com.example.foruminforexchange.service.FileStorageService;
import com.example.foruminforexchange.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    @Autowired
    private final CommentRepo commentRepo;
    @Autowired
    private final UserRepo userRepo;
    @Autowired
    private final SecurityUtil securityUtil;
    @Autowired
    private final LikeRepo likeRepo;
    @Autowired
    private final ActivityRepo activityRepo;

    @Autowired
    private final BookmarkRepo bookmarkRepo;

    @Autowired
    private final NotificationRepo notificationRepo;
    @Autowired
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Override
    public List<LikeDto> getAllLikeByComment(Long commentId) {
        List<Like> likes = likeRepo.findAllByCommentCommentId(commentId);
        if(likes == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        List<LikeDto> likeDtos = likes.stream().map(like -> LikeMapper.convertToLikeDto(like)).collect(Collectors.toList());
        return likeDtos;
    }

    @Override
    public String likeComment(Long commentId) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepo.findUserByEmail(currentUserEmail);
        Comment comment = commentRepo.findByCommentId(commentId);
        if(comment == null){
            throw new AppException(ErrorCode.COMMENT_NOT_FOUND);
        }
        Like liked = likeRepo.findByUserUserIdAndCommentCommentId(user.getUserId(), commentId);
        if(liked != null){
            return "Cannot like second time";
        }
        Like like = new Like();
        like.setCreateAt(LocalDateTime.now());
        like.setComment(comment);
        like.setUser(user);
        likeRepo.save(like);
        comment.setLikeCount(comment.getLikeCount() == 0 ? 1 : comment.getLikeCount() + 1);
        commentRepo.save(comment);

        Activity activity = new Activity();
        activity.setCreatedAt(like.getCreateAt());
        activity.setType("like");
        activity.setContent(String.format(
                "Bạn đã thích bình luận: '%s'",
                comment.getContent()
        ));
        activity.setLink(String.format("/category/%d/detail-post/%d/page/%d", comment.getPost().getCategory().getCategoryId(), comment.getPost().getPostId(), findCommentPage(comment.getPost().getPostId(), commentId, 10)));
        activity.setUser(user);
        activityRepo.save(activity);

        if(!user.getUserId().equals(comment.getUser().getUserId())){
            Notification ownerNotification = new Notification();
            ownerNotification.setUser(comment.getUser());
            ownerNotification.setCreateAt(LocalDateTime.now());
            ownerNotification.setStatus(0L);
            ownerNotification.setType(0L);
            ownerNotification.setNotificationContent(String.format(
                    "%s %s đã thích bình luận của bạn: '%s'",
                    user.getFirstName(), user.getLastName(), comment.getContent()
            ));
            ownerNotification.setLink(String.format("/category/%d/detail-post/%d/page/%d", comment.getPost().getCategory().getCategoryId(), comment.getPost().getPostId(), findCommentPage(comment.getPost().getPostId(), commentId, 10)));
            if (comment.getUser() != null) {
                notificationRepo.save(ownerNotification);
                simpMessagingTemplate.convertAndSendToUser(comment.getUser().getUserId().toString(), "/topic/notifications", NotificationMapper.convertToNotificationDto(ownerNotification));
            } else {
                throw new AppException(ErrorCode.USER_NOT_FOUND);
            }
        }

        return "Like succesfully";
    }

    @Override
    public String unlikeComment(Long commentId) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepo.findUserByEmail(currentUserEmail);
        Comment comment = commentRepo.findByCommentId(commentId);
        if(comment == null){
            throw new AppException(ErrorCode.COMMENT_NOT_FOUND);
        }
        Like like = likeRepo.findByUserUserIdAndCommentCommentId(user.getUserId(), commentId);
        if(like == null){
            return "You have not liked this post yet";
        }
        likeRepo.delete(like);
        comment.setLikeCount(comment.getLikeCount() == 0 ? 1 : comment.getLikeCount() + 1);
        commentRepo.save(comment);
        return "Unlike succesfully";
    }

    public int findCommentPage(Long postId, Long commentId, int pageSize) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("createAt").ascending());
        Page<Comment> commentsPage = commentRepo.findByPostPostId(postId, pageable);

        List<Comment> allComments = commentsPage.getContent();
        int index = IntStream.range(0, allComments.size())
                .filter(i -> allComments.get(i).getCommentId().equals(commentId))
                .findFirst()
                .orElse(-1);

        if (index != -1) {
            return index / pageSize + 1;
        } else {
            return -1;
        }
    }

}
