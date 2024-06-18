package com.example.foruminforexchange.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Polls")
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_id")
    private Long pollId;

    @Column(nullable = false, length = 5000)
    private String question;

    @Column(name = "maximum_selectable_responses")
    private Long maximumSelectableResponses;
    @Column(name = "is_unlimited")
    private Boolean isUnlimited;

    @Column(name = "change_vote")
    private Boolean changeVote;

    @Column(name = "view_results_no_vote")
    private Boolean viewResultsNoVote;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Response> responses;

    private Long timeVote = 3L;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    public Poll() {
    }

    public Poll(String question, Long maximumSelectableResponses, Boolean isUnlimited, Boolean changeVote, Boolean viewResultsNoVote, Post post) {
        this.question = question;
        this.maximumSelectableResponses = maximumSelectableResponses;
        this.isUnlimited = isUnlimited;
        this.changeVote = changeVote;
        this.viewResultsNoVote = viewResultsNoVote;
        this.post = post;
    }

    // Getters
    public Long getPollId() {
        return pollId;
    }

    public String getQuestion() {
        return question;
    }

    public Long getMaximumSelectableResponses() {
        return maximumSelectableResponses;
    }

    public Boolean getIsUnlimited() {
        return isUnlimited;
    }

    public Boolean getChangeVote() {
        return changeVote;
    }

    public Boolean getViewResultsNoVote() {
        return viewResultsNoVote;
    }

    public Post getPost() {
        return post;
    }

    public List<Response> getResponses() {
        return responses;
    }

    // Setters
    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setMaximumSelectableResponses(Long maximumSelectableResponses) {
        this.maximumSelectableResponses = maximumSelectableResponses;
    }

    public void setIsUnlimited(Boolean isUnlimited) {
        this.isUnlimited = isUnlimited;
    }

    public void setChangeVote(Boolean changeVote) {
        this.changeVote = changeVote;
    }

    public void setViewResultsNoVote(Boolean viewResultsNoVote) {
        this.viewResultsNoVote = viewResultsNoVote;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    public Long getTimeVote() {
        return timeVote;
    }

    public void setTimeVote(Long timeVote) {
        this.timeVote = timeVote;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
