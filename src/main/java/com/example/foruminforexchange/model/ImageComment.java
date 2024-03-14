package com.example.foruminforexchange.model;
import jakarta.persistence.*;

@Entity
@Table(name = "Image_Comment")
public class ImageComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_comment_id")
    private Long imageCommentId;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public ImageComment() {
    }

    public ImageComment(String imageUrl, Comment comment) {
        this.imageUrl = imageUrl;
        this.comment = comment;
    }

    // Getters
    public Long getImageCommentId() {
        return imageCommentId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Comment getComment() {
        return comment;
    }

    // Setters
    public void setImageCommentId(Long imageCommentId) {
        this.imageCommentId = imageCommentId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}

