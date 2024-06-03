package com.example.foruminforexchange.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(name = "title", nullable = false, length = 5000)
    private String title;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "comment_count")
    private Long commentCount;

    @Column(name = "report_count")
    private Long reportCount;

    @Column(name = "share_count")
    private Long shareCount;

    @Column(name = "is_locked")
    private Boolean isLocked = false;

    @Column(name = "lock_by")
    private String lockedBy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "prefix_id")
    private Prefix prefix;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Poll poll;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> report;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // Constructors
    public Post() {
        this.viewCount = 0L;
        this.likeCount = 0L;
        this.shareCount = 0L;
        this.reportCount = 0L;
    }

    public Post(String title, User user, Prefix prefix, Category category) {
        this.title = title;
        this.viewCount = 0L;
        this.likeCount = 0L;
        this.shareCount = 0L;
        this.reportCount = 0L;
        this.user = user;
        this.prefix = prefix;
        this.category = category;
    }

    // Getters and Setters
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public Long getShareCount() {
        return shareCount;
    }

    public void setShareCount(Long shareCount) {
        this.shareCount = shareCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Prefix getPrefix() {
        return prefix;
    }

    public void setPrefix(Prefix prefix) {
        this.prefix = prefix;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public Boolean isLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean locked) {
        isLocked = locked;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public Long getReportCount() {
        return reportCount;
    }

    public void setReportCount(Long reportCount) {
        this.reportCount = reportCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Report> getReport() {
        return report;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }

    public void setReport(List<Report> report) {
        this.report = report;
    }
}
