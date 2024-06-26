package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.model.Post;
import com.example.foruminforexchange.model.Prefix;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    Page<PostDto> getPostByReport(Pageable pageable);

    Page<PostDto> getAllPost(Pageable pageable);

    Page<PostDto> getAllPostsByCategory(Long categoryId, Pageable pageable);

    TopicPrefixResponse getPrefixAndTopic(Long categoryId);
    PostDto showDetailPost(Long postId);
    CreatePostResponse createPost(CreatePostRequest createPostRequest, List<MultipartFile> imageFiles);

    EditPostResponse editPost(EditPostRequest editPostRequest, List<MultipartFile> imageFiles);

    String deletePost(Long postId);

    String lockPost(Long postId);

    String unlockPost(Long postId);

    String reportPost(ReportPostRequest reportPostRequest);

}
