package com.example.foruminforexchange.controller;

import com.example.foruminforexchange.dto.ApiResponse;
import com.example.foruminforexchange.dto.BookmarkDto;
import com.example.foruminforexchange.dto.CommentDto;
import com.example.foruminforexchange.service.BookrmarkService;
import com.example.foruminforexchange.service.impl.BookmarkServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookrmarkService bookrmarkService;

    @PostMapping("/bookmark-post")
    public ApiResponse<BookmarkDto> bookmarkPost(@RequestParam("postId") Long postId){
        ApiResponse<BookmarkDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(bookrmarkService.bookmarkPost(postId));
        return apiResponse;
    }

    @PostMapping("/unbookmark-post")
    public ApiResponse<String> unbookmarkPost(@RequestParam("postId") Long postId){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(bookrmarkService.unbookmarkPost(postId));
        return apiResponse;
    }

    @GetMapping("/all-bookmark-post")
    public ApiResponse<Page<BookmarkDto>> getAllBookmarkPost(Pageable pageable){
        ApiResponse<Page<BookmarkDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(bookrmarkService.getAllBookmarkPost(pageable));
        return apiResponse;
    }

    @GetMapping("/get-bookmark-by-post")
    public ApiResponse<Boolean> getBookmarkByPostAndUser(@RequestParam Long postId){
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        apiResponse.setResult(bookrmarkService.getBookmarkByPostAndUser(postId));
        return apiResponse;
    }
}
