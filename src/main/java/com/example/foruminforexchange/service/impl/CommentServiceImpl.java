package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.configuration.SecurityUtil;
import com.example.foruminforexchange.dto.CommentDto;
import com.example.foruminforexchange.dto.CreateCommentRequest;
import com.example.foruminforexchange.dto.DeleteCommentRequest;
import com.example.foruminforexchange.dto.EditCommentRequest;
import com.example.foruminforexchange.mapper.CommentMapper;
import com.example.foruminforexchange.mapper.NotificationMapper;
import com.example.foruminforexchange.mapper.PostMapper;
import com.example.foruminforexchange.model.*;
import com.example.foruminforexchange.repository.*;
import com.example.foruminforexchange.service.CommentService;
import com.example.foruminforexchange.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    @Autowired
    private final PostRepo postRepo;
    @Autowired
    private final CommentRepo commentRepo;
    @Autowired
    private final UserRepo userRepo;
    @Autowired
    private final SecurityUtil securityUtil;
    @Autowired
    private final ImageCommentRepo imageCommentRepo;
    @Autowired
    private final ActivityRepo activityRepo;
    @Autowired
    private final FileStorageService fileStorageService;
    @Autowired
    private final BookmarkRepo bookmarkRepo;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private NotificationRepo notificationRepo;

    @Override
    public Page<CommentDto> getAllCommentByPost(Long postId, Pageable pageable) {
        if (pageable == null || pageable.getPageSize() <= 0) {
            pageable = PageRequest.of(0, 10, Sort.by("commentId").ascending());
        } else {
            int pageSize = Math.min(pageable.getPageSize(), 10);
            pageable = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort().and(Sort.by("commentId").ascending()));
        }
        if(postId == null){
            throw new AppException(ErrorCode.NOT_BLANK);
        }

        Page<Comment> comments = commentRepo.findByPostPostId(postId, pageable);
        if(comments == null){
            throw new AppException(ErrorCode.COMMENT_NOT_FOUND);
        }
        Page<CommentDto> commentDtos = comments.map(comment -> CommentMapper.convertToCommentDto(comment));
        return commentDtos;
    }

    @Transactional
    @Override
    public CommentDto createComment(CreateCommentRequest createCommentRequest, List<MultipartFile> imageFiles){
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        } else {
            Post post = postRepo.findByPostId(createCommentRequest.getPostId());
            if (post == null) {
                throw new AppException(ErrorCode.POST_NOT_FOUND);
            }
            User user = userRepo.findUserByEmail(currentUserEmail);

            Comment comment = new Comment();
            comment.setPost(post);
            comment.setUser(user);
            comment.setCreateAt(LocalDateTime.now());
            comment.setContent(createCommentRequest.getContent());
            Comment savedComment = commentRepo.save(comment);

            if(imageFiles != null && !imageFiles.isEmpty()){
                for (MultipartFile file : imageFiles) {
                    String imageUrl = fileStorageService.storeFile(file);
                    ImageComment imageComment = new ImageComment(imageUrl, savedComment);
                    imageCommentRepo.save(imageComment);
                }
            }


            post.setCommentCount(post.getCommentCount() + 1);
            postRepo.save(post);

            Activity activity = new Activity();
            activity.setCreatedAt(comment.getCreateAt());
            activity.setPost(post);
            activity.setType("comment");
            activity.setContent(String.format(
                    "Bạn đã bình luận về bài viết: '%s' với nội dung: '%s'",
                    post.getTitle(), comment.getContent()
            ));
            activity.setLink(String.format("/category/%d/detail-post/%d/page/%d", comment.getPost().getCategory().getCategoryId(), comment.getPost().getPostId(), findCommentPage(comment.getPost().getPostId(), comment.getCommentId(), 10)));
            activity.setUser(user);
            activityRepo.save(activity);

            if(!user.getUserId().equals(post.getUser().getUserId())){
                Notification ownerNotification = new Notification();
                ownerNotification.setUser(post.getUser());
                ownerNotification.setCreateAt(LocalDateTime.now());
                ownerNotification.setStatus(0L);
                ownerNotification.setType(0L);
                ownerNotification.setNotificationContent(String.format(
                        "%s %s đã bình luận về bài viết của bạn: '%s' với nội dung: '%s'",
                        user.getFirstName(), user.getLastName(), post.getTitle(), comment.getContent()
                ));
                ownerNotification.setLink(String.format("/category/%d/detail-post/%d/page/%d", comment.getPost().getCategory().getCategoryId(), comment.getPost().getPostId(), findCommentPage(comment.getPost().getPostId(), comment.getCommentId(), 10)));
                if (post.getUser() != null) {
                    notificationRepo.save(ownerNotification);
                    System.out.println(post.getUser().getUserId().toString() + " aaaaa");
                    simpMessagingTemplate.convertAndSendToUser(post.getUser().getUserId().toString(), "/topic/notifications", NotificationMapper.convertToNotificationDto(ownerNotification));
                } else {
                    throw new AppException(ErrorCode.USER_NOT_FOUND);
                }
            }

            List<User> bookmarkUsers = bookmarkRepo.findUserByBookmarkedPost(post.getPostId());
            for(User us: bookmarkUsers){
                Notification notification = new Notification();
                notification.setUser(us);
                notification.setCreateAt(LocalDateTime.now());
                notification.setStatus(0L);
                notification.setType(0L);
                notification.setNotificationContent(String.format(
                        "%s %s đã bình luận về bài viết: '%s' mà bạn quan tâm với nội dung: '%s'",
                        user.getFirstName(), user.getLastName(), post.getTitle(), comment.getContent()
                ));
                notification.setLink(String.format("/category/%d/detail-post/%d/page/%d", comment.getPost().getCategory().getCategoryId(), comment.getPost().getPostId(), findCommentPage(comment.getPost().getPostId(), comment.getCommentId(), 10)));

                notificationRepo.save(notification);
                simpMessagingTemplate.convertAndSendToUser(us.getUserId().toString(), "/topic/notifications", NotificationMapper.convertToNotificationDto(notification));
            }

            Comment reloadedComment = commentRepo.findByCommentIdWithImages(savedComment.getCommentId());

            if (reloadedComment != null) {
                return CommentMapper.convertToCommentDto(reloadedComment);
            } else {
                throw new AppException(ErrorCode.COMMENT_NOT_FOUND);
            }
        }
    }



    @Override
    public CommentDto editComment(EditCommentRequest editCommentRequest, List<MultipartFile> imageFiles) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        User user = userRepo.findUserByEmail(currentUserEmail);
        Comment comment = commentRepo.findByCommentIdAndPostPostIdAndUserUserId(editCommentRequest.getCommentId(), editCommentRequest.getPostId(), user.getUserId());
        if(comment == null){
            throw new AppException(ErrorCode.COMMENT_NOT_FOUND);
        }

        comment.setContent(editCommentRequest.getContent());
        comment.setUpdateAt(LocalDateTime.now());


        List<ImageComment> existingImages = imageCommentRepo.findAllByCommentCommentId(comment.getCommentId());
        for (ImageComment image : existingImages) {
            fileStorageService.deleteFile(image.getImageUrl());
            imageCommentRepo.delete(image);
        }

        // Lưu hình ảnh mới và cập nhật cơ sở dữ liệu
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                String imageUrl = fileStorageService.storeFile(file);
                ImageComment newImageComment = new ImageComment(imageUrl, comment);
                imageCommentRepo.save(newImageComment);
            }
        }

        Comment savedComment = commentRepo.save(comment);
        return CommentMapper.convertToCommentDto(savedComment);
    }

    @Override
    public String deleteComment(Long postId, Long commentId) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if(postId == null || commentId == null){
            throw new AppException(ErrorCode.NOT_BLANK);
        }
        User user = userRepo.findUserByEmail(currentUserEmail);
        Comment comment = commentRepo.findByCommentIdAndPostPostIdAndUserUserId(commentId, postId, user.getUserId());
        if(comment == null){
            return "Comment not exited";
        }
        commentRepo.delete(comment);
        return "Delete Successfully";
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
