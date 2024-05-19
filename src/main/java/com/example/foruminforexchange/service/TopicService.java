package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.CreateTopicRequest;
import com.example.foruminforexchange.dto.EditTopicRequest;
import com.example.foruminforexchange.dto.TopicDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TopicService {

    Page<TopicDto> getAllTopic(Pageable pageable);
    List<TopicDto> getAllListTopic();
    TopicDto createTopic(CreateTopicRequest createTopicRequest);
    TopicDto editTopic(EditTopicRequest editTopicRequest);
    String deleteTopic(Long topicId);
}
