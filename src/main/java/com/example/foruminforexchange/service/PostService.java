package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.model.Post;
import com.example.foruminforexchange.model.Prefix;

import java.util.List;

public interface PostService {

    List<Post> getAllPostsByCategory(Long categoryId);

    TopicPrefixResponse getPrefixAndTopic(Long categoryId);
    PostDto showDetailPost(Long postId);
    CreatePostResponse createPost(CreatePostRequest createPostRequest);

    EditPostResponse editPost(EditPostRequest editPostRequest);

    String deletePost(DeletePostRequest deletePostRequest);

    String lockPost(Long postId);

    String unlockPost(Long postId);
    CommentDto createComment(CreateCommentRequest createCommentRequest);

    CommentDto editComment(EditCommentRequest editCommentRequest);

    String deleteComment(DeleteCommentRequest deleteCommentRequest);

    String likePost(Long postId);

    String unlikePost(Long postId);

    String reportPost(ReportPostRequest reportPostRequest);
}
