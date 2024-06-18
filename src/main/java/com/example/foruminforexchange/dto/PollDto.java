package com.example.foruminforexchange.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PollDto {
    private Long pollId;
    private String question;
    private Long maximumSelectableResponses;
    private Boolean isUnlimited;
    private Boolean changeVote;
    private Boolean viewResultsNoVote;
    private List<ResponseDto> responses;
    private Long timeVote;
    private LocalDateTime createAt;
}
