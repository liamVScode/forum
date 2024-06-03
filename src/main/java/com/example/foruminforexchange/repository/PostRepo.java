package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface PostRepo extends JpaRepository<Post, Long>, JpaSpecificationExecutor {
    Post findByPostId(Long postId);

    Page<Post> findByReportCountGreaterThan(Long reportCount, Pageable pageable);
    Page<Post> findByCategoryCategoryId(Long categoryId, Pageable pageable);

    Long countAllByCategoryCategoryId(Long categoryId);

    List<Post> findAllByCategoryCategoryId(Long categoryId);

    @Query("SELECT COUNT(p) FROM Post p")
    Long countAll();

    Long countAllByUserUserId(Long userId);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.createAt BETWEEN :startDate AND :endDate")
    Long countByCreateAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
