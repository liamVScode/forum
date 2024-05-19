package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.configuration.SecurityUtil;
import com.example.foruminforexchange.dto.ResponseDto;
import com.example.foruminforexchange.dto.UpdateVoteRequest;
import com.example.foruminforexchange.dto.VoteRequest;
import com.example.foruminforexchange.mapper.ResponseMapper;
import com.example.foruminforexchange.model.Response;
import com.example.foruminforexchange.model.ResponseUser;
import com.example.foruminforexchange.model.User;
import com.example.foruminforexchange.repository.ResponseRepo;
import com.example.foruminforexchange.repository.ResponseUserRepo;
import com.example.foruminforexchange.repository.UserRepo;
import com.example.foruminforexchange.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {

    private final ResponseUserRepo responseUserRepo;
    private final ResponseRepo responseRepo;
    private final SecurityUtil securityUtil;
    private final UserRepo userRepo;
    @Override
    public List<ResponseDto> getAllResponseByPoll(Long pollId) {
        if(pollId == null){
            throw new AppException(ErrorCode.NOT_BLANK);
        }
        List<Response> responses = responseRepo.findAllByPollPollId(pollId);
        if(responses == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        //tong vote cua poll
        Long totalVotes = responses.stream()
                .mapToLong(Response::getVoteCount)
                .sum();

        List<ResponseDto> responseDtos = responses.stream()
                .map(response -> {
                    double percentage = 0.0;
                    if (response.getVoteCount() != null && response.getVoteCount() > 0 && totalVotes > 0) {
                        percentage = (double) response.getVoteCount() * 100 / totalVotes;
                    }
                    return ResponseMapper.convertToResponseDto(response, percentage);
                })
                .collect(Collectors.toList());

        return responseDtos;
    }

    @Override
    public String voteResponse(VoteRequest voteRequest) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepo.findUserByEmail(currentUserEmail);
        if(user == null){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        for (Long responseId : voteRequest.getResponseId()) {
            Response response = responseRepo.findById(responseId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
            ResponseUser responseUser = responseUserRepo.findByResponseResponseIdAndUserUserId(responseId, user.getUserId());
            if (responseUser != null) {
                continue;
            }
            responseUser = new ResponseUser();
            responseUser.setResponse(response);
            responseUser.setUser(user);
            responseUser.setVoteTime(LocalDateTime.now());
            responseUserRepo.save(responseUser);

            response.setVoteCount(response.getVoteCount() + 1);
            responseRepo.save(response);
        }
        return "Voted";
    }

    @Override
    public String updateVoteResponse(UpdateVoteRequest updateVoteRequest) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepo.findUserByEmail(currentUserEmail);
        if(user == null){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        List<ResponseUser> existingVotes = responseUserRepo.findByUserUserIdAndResponsePollPollId(user.getUserId(), updateVoteRequest.getPollId());
        existingVotes.forEach(vote -> {
            Response response = vote.getResponse();
            response.setVoteCount(response.getVoteCount() - 1);
            responseRepo.save(response);
            responseUserRepo.delete(vote);
        });

        for (Long responseId : updateVoteRequest.getResponseId()) {
            Response response = responseRepo.findById(responseId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
            ResponseUser newVote = new ResponseUser();
            newVote.setResponse(response);
            newVote.setUser(user);
            newVote.setVoteTime(LocalDateTime.now());
            responseUserRepo.save(newVote);

            response.setVoteCount(response.getVoteCount() + 1);
            responseRepo.save(response);
        }

        return "Updated vote";
    }
}
