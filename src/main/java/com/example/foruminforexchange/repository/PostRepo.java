package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Long>, JpaSpecificationExecutor {
    Post findByPostId(Long postId);

    Page<Post> findByReportCountGreaterThan(Long reportCount, Pageable pageable);
    Page<Post> findByCategoryCategoryId(Long categoryId, Pageable pageable);
}
