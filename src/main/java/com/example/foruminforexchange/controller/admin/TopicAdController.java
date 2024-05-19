package com.example.foruminforexchange.controller.admin;

import com.example.foruminforexchange.dto.ApiResponse;
import com.example.foruminforexchange.dto.CreateTopicRequest;
import com.example.foruminforexchange.dto.EditTopicRequest;
import com.example.foruminforexchange.dto.TopicDto;
import com.example.foruminforexchange.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/topics")
@CrossOrigin(origins = {"http://localhost:4200", "https://localhost:4200"})
public class TopicAdController {

    private final TopicService topicService;

    @GetMapping("/all-topic")
    public ApiResponse<Page<TopicDto>> getAllTopic(Pageable pageable){
        ApiResponse<Page<TopicDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(topicService.getAllTopic(pageable));
        return apiResponse;
    }

    @GetMapping("/list-topic")
    public ApiResponse<List<TopicDto>> getAllTopic(){
        ApiResponse<List<TopicDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(topicService.getAllListTopic());
        return apiResponse;
    }
    @PostMapping("/create-topic")
    public ApiResponse<TopicDto> createTopic(@RequestBody CreateTopicRequest createTopicRequest){
        ApiResponse<TopicDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(topicService.createTopic(createTopicRequest));
        return apiResponse;
    }

    @PutMapping("/edit-topic")
    public ApiResponse<TopicDto> editTopic(@RequestBody EditTopicRequest editTopicRequest){
        ApiResponse<TopicDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(topicService.editTopic(editTopicRequest));
        return apiResponse;
    }

    @DeleteMapping("/delete-topic")
    public ApiResponse<String> deleteTopic(@RequestParam("topicId") Long topicId){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(topicService.deleteTopic(topicId));
        return apiResponse;
    }
}
