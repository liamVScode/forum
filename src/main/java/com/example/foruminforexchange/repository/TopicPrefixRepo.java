package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.TopicPrefix;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicPrefixRepo extends JpaRepository<TopicPrefix, Long> {
    TopicPrefix findByTopicTopicId(Long topicId);
    TopicPrefix findByPrefixPrefixId(Long prefixId);
}
