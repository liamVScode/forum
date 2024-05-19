package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Bookmark;
import com.example.foruminforexchange.model.Post;
import com.example.foruminforexchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookmarkRepo extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByPostPostId(Long postId);

    Bookmark findByUserUserIdAndPostPostId(Long userId, Long postId);

    @Query("SELECT b.user FROM Bookmark b WHERE b.post.postId = :postId")
    List<User> findUserByBookmarkedPost(Long postId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Bookmark ic WHERE ic.post.postId = :postId")
    void deleteAllByPostPostId(Long postId);
}
