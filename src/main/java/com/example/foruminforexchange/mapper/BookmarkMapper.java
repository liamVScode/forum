package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.BookmarkDto;
import com.example.foruminforexchange.model.Bookmark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class BookmarkMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PostMapper postMapper;
    public BookmarkDto convertToBookmarkDto(Bookmark bookmark){
        BookmarkDto bookmarkDto = new BookmarkDto();
        bookmarkDto.setBookmarkId(bookmark.getBookmarkId());
        bookmarkDto.setUser(userMapper.convertToUserDto(bookmark.getUser()));
        bookmarkDto.setPost(postMapper.convertToPostDto(bookmark.getPost()));
        return bookmarkDto;
    }
}
