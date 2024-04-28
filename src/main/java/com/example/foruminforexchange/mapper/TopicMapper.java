package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.TopicDto;
import com.example.foruminforexchange.model.Topic;

public class TopicMapper {
    public static TopicDto convertToTopicDto(Topic topic){
        TopicDto topicDto = new TopicDto();
        topicDto.setTopicId(topic.getTopicId());
        topicDto.setTopicName(topic.getTopicName());
        return topicDto;
    }
}
