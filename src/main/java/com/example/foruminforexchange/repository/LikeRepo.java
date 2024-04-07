package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepo extends JpaRepository<Like, Long> {
    Like findByUserUserIdAndPostPostId(Long userId, Long postId);
}
