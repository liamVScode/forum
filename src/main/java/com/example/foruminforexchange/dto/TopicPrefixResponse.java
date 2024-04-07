package com.example.foruminforexchange.dto;

import com.example.foruminforexchange.model.Prefix;
import com.example.foruminforexchange.model.Topic;
import lombok.Data;

import java.util.List;
@Data
public class TopicPrefixResponse {
    TopicDto topic;
    List<PrefixDto> prefix;
}
