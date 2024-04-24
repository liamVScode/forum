package com.example.foruminforexchange.controller;

import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.model.Post;
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
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {

    private final PostService postService;

    private final SearchService searchService;

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

    @PostMapping("/create-comment")
    public ApiResponse<CommentDto> createComment(@ModelAttribute CreateCommentRequest createCommentRequest, @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles){

        ApiResponse<CommentDto> apiResponse = new ApiResponse<>();

        apiResponse.setResult(postService.createComment(createCommentRequest, imageFiles));

        return apiResponse;
    }

    @PutMapping("/edit-comment")
    public ApiResponse<CommentDto> createComment(@ModelAttribute EditCommentRequest editCommentRequest, @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles){

        ApiResponse<CommentDto> apiResponse = new ApiResponse<>();

        apiResponse.setResult(postService.editComment(editCommentRequest, imageFiles));

        return apiResponse;
    }

    @PostMapping("/delete-comment")
    public ApiResponse<String> createComment(@RequestBody DeleteCommentRequest deleteCommentRequest){

        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setResult(postService.deleteComment(deleteCommentRequest));

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

    @DeleteMapping("/delete-post")
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


    @PostMapping("/like-post")
    public ApiResponse<String> likePost(@RequestParam("postId") Long postId){
        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setResult(postService.likePost(postId));

        return apiResponse;
    }

    @PostMapping("/unlike-post")
    public ApiResponse<String> unlikePost(@RequestParam("postId") Long postId){
        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setResult(postService.unlikePost(postId));

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


}
