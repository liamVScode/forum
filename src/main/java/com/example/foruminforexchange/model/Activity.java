package com.example.foruminforexchange.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "Activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long activityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    @Column(name = "activity_type")
    private String type;

    @Column(name = "activity_content", nullable = true)
    private String content;

    @Column(name = "activity_time")
    private LocalDateTime createdAt;

    public Activity(User user, Post post, String type, String content, LocalDateTime createdAt) {
        this.user = user;
        this.post = post;
        this.type = type;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Activity() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String type) {
        if(type.equals("like"))
            this.content = "Like bài viết";
        if(type.equals("report"))
            this.content = "Report bài viết";
    }

    public void setContentComment(String content){
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return activityId;
    }
}
