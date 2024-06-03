package com.example.foruminforexchange.controller;

import com.example.foruminforexchange.dto.ApiResponse;
import com.example.foruminforexchange.dto.ResponseDto;
import com.example.foruminforexchange.dto.UpdateVoteRequest;
import com.example.foruminforexchange.dto.VoteRequest;
import com.example.foruminforexchange.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/polls/responses")
@RequiredArgsConstructor
public class ResponseController {

    private final ResponseService responseService;

    @GetMapping("/get-response-by-poll")
    public ApiResponse<List<ResponseDto>> getResponsesByPoll(@RequestParam("pollId") Long pollId){
        ApiResponse<List<ResponseDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(responseService.getAllResponseByPoll(pollId));
        return apiResponse;
    }

    @PostMapping("/vote")
    public ApiResponse<String> voteResponse(@RequestBody VoteRequest voteRequest){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(responseService.voteResponse(voteRequest));
        return apiResponse;
    }

    @PostMapping("/update-vote")
    public ApiResponse<String> updatevoteResponse(@RequestBody UpdateVoteRequest updateVoteRequest){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(responseService.updateVoteResponse(updateVoteRequest));
        return apiResponse;
    }

}
