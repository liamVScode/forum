package com.example.foruminforexchange.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditTopicRequest {
    Long topicId;
    String topicName;
}
