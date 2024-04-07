package com.example.foruminforexchange.dto;

import lombok.Data;

import java.util.List;

@Data
public class PollRequest {
    String question;
    Long maximum_selectable_responses;
    boolean isUnLimited;
    boolean changeVote;
    boolean viewResultNoVote;
    Long postId;
    List<ResponseDto> responseDtos;
}
