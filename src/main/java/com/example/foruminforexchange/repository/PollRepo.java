package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Poll;
import com.example.foruminforexchange.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PollRepo extends JpaRepository<Poll, Long> {
    Poll findByPostPostId(Long postId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Poll ic WHERE ic.post.postId = :postId")
    void deleteByPostId(Long postId);
}
