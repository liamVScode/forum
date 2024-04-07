package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Long> {
    Post findByPostId(Long postId);

    List<Post> findByCategoryCategoryId(Long categoryId);
}
