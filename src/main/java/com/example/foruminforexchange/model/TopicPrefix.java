package com.example.foruminforexchange.model;
import jakarta.persistence.*;

@Entity
@Table(name = "Topic_Prefix")
public class TopicPrefix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicPrefixId;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "prefix_id", nullable = false)
    private Prefix prefix;

    // Constructors
    public TopicPrefix() {
    }

    public TopicPrefix(Topic topic, Prefix prefix) {
        this.topic = topic;
        this.prefix = prefix;
    }

    // Getters and Setters
    public Long getTopicPrefixId() {
        return topicPrefixId;
    }

    public void setTopicPrefixId(Long topicPrefixId) {
        this.topicPrefixId = topicPrefixId;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Prefix getPrefix() {
        return prefix;
    }

    public void setPrefix(Prefix prefix) {
        this.prefix = prefix;
    }
}
