package com.example.foruminforexchange.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Column(length = 255)
    private String reportContent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Report() {
    }

    public Report(String reportContent, User user, Post post) {
        this.reportContent = reportContent;
        this.user = user;
        this.post = post;
    }

    // Getters
    public Long getReportId() {
        return reportId;
    }

    public String getReportContent() {
        return reportContent;
    }

    public User getUser() {
        return user;
    }

    public Post getPost() {
        return post;
    }

    // Setters
    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
