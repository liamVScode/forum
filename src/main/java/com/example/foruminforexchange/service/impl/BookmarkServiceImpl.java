package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.configuration.SecurityUtil;
import com.example.foruminforexchange.dto.BookmarkDto;
import com.example.foruminforexchange.mapper.BookmarkMapper;
import com.example.foruminforexchange.model.Bookmark;
import com.example.foruminforexchange.model.Post;
import com.example.foruminforexchange.model.User;
import com.example.foruminforexchange.repository.BookmarkRepo;
import com.example.foruminforexchange.repository.PostRepo;
import com.example.foruminforexchange.repository.UserRepo;
import com.example.foruminforexchange.service.BookrmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookrmarkService {

    private final UserRepo userRepo;

    private final PostRepo postRepo;

    private final BookmarkRepo bookmarkRepo;

    private final SecurityUtil securityUtil;
    @Override
    public BookmarkDto bookmarkPost(Long postId) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        User user = userRepo.findUserByEmail(currentUserEmail);
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail) || user == null){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if(postId == null){
            throw new AppException(ErrorCode.NOT_BLANK);
        }
        Post post = postRepo.findByPostId(postId);
        if(post == null){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }
        Bookmark bookmark = new Bookmark();
        bookmark.setUser(user);
        bookmark.setPost(post);
        Bookmark bookmarkSaved = bookmarkRepo.save(bookmark);
        return BookmarkMapper.convertToBookmarkDto(bookmarkSaved);
    }

    @Override
    public String unbookmarkPost(Long postId) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        User user = userRepo.findUserByEmail(currentUserEmail);
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail) || user == null){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if(postId == null){
            throw new AppException(ErrorCode.NOT_BLANK);
        }
        Post post = postRepo.findByPostId(postId);
        if(post == null){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }
        Bookmark bookmark = bookmarkRepo.findByUserUserIdAndPostPostId(user.getUserId(), postId);
        if(bookmark == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        bookmarkRepo.delete(bookmark);
        return "Unbookmark successfully!";
    }
}
