package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.dto.CreateTopicRequest;
import com.example.foruminforexchange.dto.EditTopicRequest;
import com.example.foruminforexchange.dto.TopicDto;
import com.example.foruminforexchange.mapper.TopicMapper;
import com.example.foruminforexchange.model.Topic;
import com.example.foruminforexchange.model.TopicPrefix;
import com.example.foruminforexchange.repository.TopicPrefixRepo;
import com.example.foruminforexchange.repository.TopicRepo;
import com.example.foruminforexchange.service.TopicService;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepo topicRepo;

    private final TopicPrefixRepo topicPrefixRepo;
    @Override
    public Page<TopicDto> getAllTopic(Pageable pageable) {
        if (pageable == null || pageable.getPageSize() <= 0) {
            pageable = PageRequest.of(0, 10, Sort.by("topicId").ascending());
        } else {
            int pageSize = Math.min(pageable.getPageSize(), 10);
            pageable = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort().and(Sort.by("topicId").ascending()));
        }
        Page<Topic> topics = topicRepo.findAll(pageable);
        if(topics == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        Page<TopicDto> topicDtos = topics.map(topic -> TopicMapper.convertToTopicDto(topic));
        return topicDtos;
    }

    @Override
    public List<TopicDto> getAllListTopic() {
        List<Topic> topics = topicRepo.findAll();
        List<TopicDto> topicDtos = topics.stream().map(topic -> TopicMapper.convertToTopicDto(topic)).collect(Collectors.toList());
        return topicDtos;
    }

    @Override
    public TopicDto createTopic(CreateTopicRequest createTopicRequest) {
        if(createTopicRequest.getTopicName() == null){
            throw new AppException(ErrorCode.NOT_BLANK);
        }
        Topic topic = new Topic(createTopicRequest.getTopicName());
        topicRepo.save(topic);
        TopicDto topicDto = TopicMapper.convertToTopicDto(topic);
        return topicDto;
    }

    @Override
    public TopicDto editTopic(EditTopicRequest editTopicRequest) {
        if(editTopicRequest.getTopicName() == null || editTopicRequest.getTopicId() == null){
            throw new AppException(ErrorCode.NOT_BLANK);
        }
        Topic topic = topicRepo.findById(editTopicRequest.getTopicId()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        topic.setTopicName(editTopicRequest.getTopicName());
        topicRepo.save(topic);
        TopicDto topicDto = TopicMapper.convertToTopicDto(topic);
        return topicDto;
    }

    @Override
    public String deleteTopic(Long topicId) {
        Topic topic = topicRepo.findById(topicId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        try {
            List<TopicPrefix> topicPrefix = topicPrefixRepo.findByTopicTopicId(topicId);
            for (TopicPrefix tp : topicPrefix) {
                topicPrefixRepo.delete(tp);
            }
            topicRepo.delete(topic);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new AppException(ErrorCode.DELETE_CONSTRAINT_VIOLATION);
        }

        return "Delete topic successfully!";
    }

}
