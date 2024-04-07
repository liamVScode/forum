package com.example.foruminforexchange.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicId;

    @Column(name = "topic_name", nullable = false, length = 255)
    private String topicName;

    @OneToMany(mappedBy = "topic")
    private List<Category> categories = new ArrayList<>();

    // Constructors
    public Topic() {
    }

    public Topic(String topicName) {
        this.topicName = topicName;
    }

    // Getters and Setters
    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}

