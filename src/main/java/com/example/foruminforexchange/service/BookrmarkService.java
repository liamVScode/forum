package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.BookmarkDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookrmarkService {
    BookmarkDto bookmarkPost(Long postId);
    String unbookmarkPost(Long postId);

    Page<BookmarkDto> getAllBookmarkPost(Pageable pageable);

    Boolean getBookmarkByPostAndUser(Long postId);
}
