package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.BookmarkDto;

public interface BookrmarkService {
    BookmarkDto bookmarkPost(Long postId);
    String unbookmarkPost(Long postId);
}
