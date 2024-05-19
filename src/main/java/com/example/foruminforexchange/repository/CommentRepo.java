package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {

    Comment findByCommentId(Long commentId);

    Page<Comment> findByPostPostId(Long postId, Pageable pageable);
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.images WHERE c.commentId = :commentId")
    Comment findByCommentIdWithImages(@Param("commentId") Long commentId);
    List<Comment> findAllByPostPostId(Long postId);
    Comment findByCommentIdAndPostPostIdAndUserUserId(Long commentId, Long postId, Long userId);
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.postId = :postId")
    Long countByPostId(@Param("postId") Long postId);
    @Query("SELECT c FROM Comment c WHERE c.post.postId = :postId ORDER BY c.createAt ASC")
    Page<Comment> findFirstCommentByPostId(@Param("postId") Long postId, Pageable pageable);
    @Modifying
    @Transactional
    @Query("DELETE FROM Comment ic WHERE ic.post.postId = :postId")
    void deleteByPostId(Long postId);

    @Query("SELECT COUNT(c) FROM Comment c")
    Long countAll();

    Long countAllByUserUserId(Long userId);
}
