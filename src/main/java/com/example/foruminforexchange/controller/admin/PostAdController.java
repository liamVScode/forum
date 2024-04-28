package com.example.foruminforexchange.controller.admin;

import com.example.foruminforexchange.dto.ApiResponse;
import com.example.foruminforexchange.dto.PostDto;
import com.example.foruminforexchange.dto.ReportDto;
import com.example.foruminforexchange.service.PostService;
import com.example.foruminforexchange.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PostAdController {

    private final PostService postService;
    private final SearchService searchService;

    @GetMapping("/all-post")
    public ApiResponse<Page<PostDto>> getAllPost(Pageable pageable){
        ApiResponse<Page<PostDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(postService.getAllPost(pageable));
        return apiResponse;
    }

    @GetMapping("/reported-post")
    public ApiResponse<Page<PostDto>> getPostByReport(Pageable pageable){
        ApiResponse<Page<PostDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(postService.getPostByReport(pageable));
        return apiResponse;
    }

    @GetMapping("filter-post")
    public ApiResponse<Page<PostDto>> filterPosts(
            @RequestParam(value = "prefixId", required = false) Long prefixId,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
            @RequestParam(value = "updateTime", required = false) Long updateTime,
            @RequestParam(value = "postType", required = false) String postType,
            @RequestParam(value = "sortField", required = false) String sortField,
            @RequestParam(value = "sortOrder", required = false) String sortOrder,
            Pageable pageable){
        ApiResponse<Page<PostDto>> apiResponse = new ApiResponse<>();

        apiResponse.setResult(searchService.filterPost(prefixId, searchKeyword, updateTime, postType,sortField, sortOrder, pageable));

        return apiResponse;
    }

    @GetMapping("/lock-post")
    public ApiResponse<String> lockPost(@RequestParam("postId") Long postId){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(postService.lockPost(postId));
        return apiResponse;
    }

    @DeleteMapping("/delete-post")
    public ApiResponse<String> deletePost(@RequestParam("postId") Long postId){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(postService.deletePost(postId));
        return apiResponse;
    }

    @PostMapping("/unlock-post")
    public ApiResponse<String> unlockPost(@RequestParam("postId") Long postId){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(postService.unlockPost(postId));
        return apiResponse;
    }

}
