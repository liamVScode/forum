package com.example.foruminforexchange.dto;

import lombok.Data;

import java.util.List;

@Data
public class ForumInforResponse {
    TopicDto topicDto;
    List<CategoryForumInfo> categoryForumInfos;
}
