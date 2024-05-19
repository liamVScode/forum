package com.example.foruminforexchange.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateVoteRequest {
    Long pollId;
    List<Long> responseId;
}
