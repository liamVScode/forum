package com.example.foruminforexchange.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Share")
public class Share {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "share_id")
    private Long shareId;

    @Column(name = "share_time", nullable = false)
    private LocalDateTime shareTime = LocalDateTime.now();
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Share(LocalDateTime shareTime, User user, Post post) {
        this.shareTime = shareTime;
        this.user = user;
        this.post = post;
    }

    public Share() {
    }

    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }

    public LocalDateTime getShareTime() {
        return shareTime;
    }

    public void setShareTime(LocalDateTime shareTime) {
        this.shareTime = shareTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
