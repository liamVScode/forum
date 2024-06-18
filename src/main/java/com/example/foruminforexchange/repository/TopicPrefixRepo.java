package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.TopicPrefix;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicPrefixRepo extends JpaRepository<TopicPrefix, Long> {
    List<TopicPrefix> findByTopicTopicId(Long topicId);
    List<TopicPrefix> findByPrefixPrefixId(Long prefixId);
}
