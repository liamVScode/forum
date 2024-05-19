package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LikeRepo extends JpaRepository<Like, Long> {
    List<Like> findAllByCommentCommentId(Long commentId);
    Like findByUserUserIdAndCommentCommentId(Long userId, Long postId);
    Long countAllByUserUserId(Long userId);
    @Modifying
    @Transactional
    @Query("DELETE FROM Like ic WHERE ic.comment.commentId = :commentId")
    void deleteByCommentCommentId(Long commentId);
}
