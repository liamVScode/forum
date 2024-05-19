package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.BookmarkDto;
import com.example.foruminforexchange.model.Bookmark;

public class BookmarkMapper {
    public static BookmarkDto convertToBookmarkDto(Bookmark bookmark){
        BookmarkDto bookmarkDto = new BookmarkDto();
        bookmarkDto.setBookmarkId(bookmark.getBookmarkId());
        bookmarkDto.setUserId(bookmark.getUser().getUserId());
        bookmarkDto.setPostId(bookmark.getPost().getPostId());
        return bookmarkDto;
    }
}
