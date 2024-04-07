package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.ImageComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageCommentRepo extends JpaRepository<ImageComment, Long> {
    List<ImageComment> findAllByCommentCommentId(Long commentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ImageComment ic WHERE ic.comment.commentId = :commentId")
    void deleteByCommentId(Long commentId);
}
