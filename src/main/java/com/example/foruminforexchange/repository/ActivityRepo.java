package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ActivityRepo extends JpaRepository<Activity, Long> {
    Page<Activity> findAllByUserUserId(Long userId, Pageable pageable);

    List<Activity> findAllByPostPostId(Long postId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Activity ac WHERE ac.post.postId = :postId")
    void deleteAllByPostPostId(Long postId);
}
