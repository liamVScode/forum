package com.example.foruminforexchange.controller;

import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.model.Post;
import com.example.foruminforexchange.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {

    private final PostService postService;

    @GetMapping("/topic-prefix")
    public ApiResponse<TopicPrefixResponse> getTopicAndPrefix(@RequestParam("categoryId") Long categoryId){
        ApiResponse<TopicPrefixResponse> apiResponse = new ApiResponse<>();

        apiResponse.setResult(postService.getPrefixAndTopic(categoryId));

        return apiResponse;
    }

    @GetMapping("/all-post")
    public ApiResponse<List<Post>>  getAllPostsByCategory(@RequestParam("categoryId") Long categoryId){
        ApiResponse<List<Post>> apiResponse = new ApiResponse<>();

        apiResponse.setResult(postService.getAllPostsByCategory(categoryId));

        return apiResponse;
    }

    @PostMapping("/create-comment")
    public ApiResponse<CommentDto> createComment(@RequestBody CreateCommentRequest createCommentRequest){

        ApiResponse<CommentDto> apiResponse = new ApiResponse<>();

        apiResponse.setResult(postService.createComment(createCommentRequest));

        return apiResponse;
    }

    @PutMapping("/edit-comment")
    public ApiResponse<CommentDto> createComment(@RequestBody EditCommentRequest editCommentRequest){

        ApiResponse<CommentDto> apiResponse = new ApiResponse<>();

        apiResponse.setResult(postService.editComment(editCommentRequest));

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
    public ApiResponse<CreatePostResponse> createPost(@RequestBody CreatePostRequest createPostRequest){
        ApiResponse<CreatePostResponse> apiResponse = new ApiResponse<>();

        apiResponse.setResult(postService.createPost(createPostRequest));

        return apiResponse;
    }

    @PutMapping("/edit-post")
    public ApiResponse<EditPostResponse> editPost(@RequestBody EditPostRequest editPostRequest){
        ApiResponse<EditPostResponse> apiResponse = new ApiResponse<>();

        apiResponse.setResult(postService.editPost(editPostRequest));

        return apiResponse;
    }

    @GetMapping("/delete-post")
    public ApiResponse<String> deletePost(@RequestBody DeletePostRequest deletePostRequest){
        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setResult(postService.deletePost(deletePostRequest));

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


}
