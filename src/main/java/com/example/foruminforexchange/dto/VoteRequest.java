package com.example.foruminforexchange.dto;

import lombok.Data;

import java.util.List;

@Data
public class VoteRequest {
    List<Long> responseId;
}
