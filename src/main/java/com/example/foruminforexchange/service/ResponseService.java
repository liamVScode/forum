package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.ResponseDto;
import com.example.foruminforexchange.dto.UpdateVoteRequest;
import com.example.foruminforexchange.dto.VoteRequest;
import com.example.foruminforexchange.model.Response;

import java.util.List;

public interface ResponseService {
    List<ResponseDto> getAllResponseByPoll(Long pollId);

    String voteResponse(VoteRequest voteRequest);

    String updateVoteResponse(UpdateVoteRequest updateVoteRequest);

}
