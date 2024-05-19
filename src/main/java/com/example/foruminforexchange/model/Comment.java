package com.example.foruminforexchange.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(nullable = false, length = 255)
    private String content;

    private Long type;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();
    @OneToMany(mappedBy = "comment", fetch = FetchType.EAGER ,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageComment> images = new ArrayList<>();

    @Column(name = "like_count")
    private Long likeCount = 0L;


    public Comment() {
        this.likeCount = 0L;
    }

    public Comment(String content, Long type, User user, Post post) {
        this.content = content;
        this.type = type;
        this.user = user;
        this.post = post;
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    // Getters
    public Long getCommentId() {
        return commentId;
    }

    public String getContent() {
        return content;
    }

    public Long getType() {
        return type;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public User getUser() {
        return user;
    }

    public Post getPost() {
        return post;
    }

    // Setters
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void addImage(ImageComment imageComment){
        images.add(imageComment);
        imageComment.setComment(this);
    }

    public void removeImage(ImageComment image) {
        images.remove(image);
        image.setComment(null);
    }

    public List<ImageComment> getImages() {
        return images;
    }

    public void setImages(List<ImageComment> images) {
        this.images = images;
    }

    public Long getLikeCount() {
        return this.likeCount != null ? this.likeCount : 0L;
    }


    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }
}
