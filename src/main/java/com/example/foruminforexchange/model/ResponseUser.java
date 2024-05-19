package com.example.foruminforexchange.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Response_User")
public class ResponseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_user_id")
    private Long responseUserId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "response_id")
    private Response response;

    @Column(name = "vote_time")
    private LocalDateTime voteTime;

    public ResponseUser() {
    }

    public ResponseUser(User user, Response response) {
        this.user = user;
        this.response = response;
    }

    public Long getResponseUserId() {
        return responseUserId;
    }

    public void setResponseUserId(Long responseUserId) {
        this.responseUserId = responseUserId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public LocalDateTime getVoteTime() {
        return voteTime;
    }

    public void setVoteTime(LocalDateTime voteTime) {
        this.voteTime = voteTime;
    }
}
