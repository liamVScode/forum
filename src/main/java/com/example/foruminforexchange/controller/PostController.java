package com.example.foruminforexchange.controller;

import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.service.PostService;
import com.example.foruminforexchange.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final SearchService searchService;

    @GetMapping("/get-all")
    public ApiResponse<Page<PostDto>> getAllPost(Pageable pageable){
        ApiResponse<Page<PostDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(postService.getAllPost(pageable));
        return apiResponse;
    }

    @GetMapping("/topic-prefix")
    public ApiResponse<TopicPrefixResponse> getTopicAndPrefix(@RequestParam("categoryId") Long categoryId){
        ApiResponse<TopicPrefixResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(postService.getPrefixAndTopic(categoryId));
        return apiResponse;
    }

    @GetMapping("/all-post")
    public ApiResponse<Page<PostDto>>  getAllPostsByCategory(@RequestParam("categoryId") Long categoryId, Pageable pageable){
        ApiResponse<Page<PostDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(postService.getAllPostsByCategory(categoryId, pageable));
        return apiResponse;
    }

    @GetMapping("/detail-post")
    public ApiResponse<PostDto> showDetailPost(@RequestParam("postId") Long postId){
        ApiResponse<PostDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(postService.showDetailPost(postId));
        return apiResponse;

    }

    @PostMapping("/create-post")
    public ApiResponse<CreatePostResponse> createPost(@ModelAttribute CreatePostRequest createPostRequest, @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles){
        ApiResponse<CreatePostResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(postService.createPost(createPostRequest, imageFiles));
        return apiResponse;
    }

    @PutMapping("/edit-post")
    public ApiResponse<EditPostResponse> editPost(@ModelAttribute EditPostRequest editPostRequest, @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles){
        ApiResponse<EditPostResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(postService.editPost(editPostRequest, imageFiles));
        return apiResponse;
    }

    @PostMapping("/delete-post")
    public ApiResponse<String> deletePost(@RequestParam("postId") Long postId){
        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setResult(postService.deletePost(postId));

        return apiResponse;
    }

    @GetMapping("/lock-post")
    public ApiResponse<String> lockPost(@RequestParam("postId") Long postId){
        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setResult(postService.lockPost(postId));

        return apiResponse;
    }

    @PostMapping("/unlock-post")
    public ApiResponse<String> unlockPost(@RequestParam("postId") Long postId){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(postService.unlockPost(postId));
        return apiResponse;
    }

    @PostMapping("report-post")
    public ApiResponse<String> reportPost(@RequestBody ReportPostRequest reportPostRequest){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(postService.reportPost(reportPostRequest));
        return apiResponse;
    }

    @GetMapping("filter-post")
    public ApiResponse<Page<PostDto>> filterPosts(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "prefixId", required = false) Long prefixId,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
            @RequestParam(value = "updateTime", required = false) Long updateTime,
            @RequestParam(value = "postType", required = false) Long postType,
            @RequestParam(value = "report", required = false) Long report,
            @RequestParam(value = "sortField", required = false) String sortField,
            @RequestParam(value = "sortOrder", required = false) String sortOrder,
            Pageable pageable){
        ApiResponse<Page<PostDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(searchService.filterPost(categoryId, prefixId, searchKeyword, updateTime, postType, report, sortField, sortOrder, pageable));
        return apiResponse;
    }


}
