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
@CrossOrigin(origins = {"http://localhost:4200", "https://localhost:4200"})
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
}
