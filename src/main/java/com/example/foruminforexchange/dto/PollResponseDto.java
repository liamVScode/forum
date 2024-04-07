package com.example.foruminforexchange.dto;

import lombok.Data;

@Data
public class PollResponseDto {
    private Long responseId;
    private String responseContent;
    private Long voteCount;
}
