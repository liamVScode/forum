package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.CreateTopicRequest;
import com.example.foruminforexchange.dto.EditTopicRequest;
import com.example.foruminforexchange.dto.TopicDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TopicService {

    Page<TopicDto> getAllTopic(Pageable pageable);
    TopicDto createTopic(CreateTopicRequest createTopicRequest);
    TopicDto editTopic(EditTopicRequest editTopicRequest);
    String deleteTopic(Long topicId);
}
