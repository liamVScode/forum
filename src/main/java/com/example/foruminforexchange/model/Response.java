package com.example.foruminforexchange.model;
import jakarta.persistence.*;

@Entity
@Table(name = "Responses")
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long responseId;

    @Column(nullable = false, length = 255)
    private String responseContent;

    private Long voteCount;

    @ManyToOne
    @JoinColumn(name = "poll_id")
    private Poll poll;

    public Response() {
    }

    public Response(String responseContent, Long voteCount, Poll poll) {
        this.responseContent = responseContent;
        this.voteCount = voteCount;
        this.poll = poll;
    }

    // Getters and setters
    public Long getResponseId() {
        return responseId;
    }

    public void setResponseId(Long responseId) {
        this.responseId = responseId;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }
}
