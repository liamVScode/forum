package com.example.foruminforexchange.model;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Prefixes")
public class Prefix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prefixId;

    @Column(name = "prefix_name", nullable = false, length = 255)
    private String prefixName;

    @OneToMany(mappedBy = "prefix", cascade = CascadeType.ALL)
    private List<TopicPrefix> topicPrefixes = new ArrayList<>();

    // Constructors
    public Prefix() {
    }

    public Prefix(String prefixName) {
        this.prefixName = prefixName;
    }

    // Getters and Setters
    public Long getPrefixId() {
        return prefixId;
    }

    public void setPrefixId(Long prefixId) {
        this.prefixId = prefixId;
    }

    public String getPrefixName() {
        return prefixName;
    }

    public void setPrefixName(String prefixName) {
        this.prefixName = prefixName;
    }

    public List<TopicPrefix> getTopicPrefixes() {
        return topicPrefixes;
    }

    public void setTopicPrefixes(List<TopicPrefix> topicPrefixes) {
        this.topicPrefixes = topicPrefixes;
    }
}

