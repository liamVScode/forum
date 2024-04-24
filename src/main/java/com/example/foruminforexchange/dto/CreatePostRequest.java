package com.example.foruminforexchange.dto;

import com.example.foruminforexchange.model.Category;
import com.example.foruminforexchange.model.Comment;
import com.example.foruminforexchange.model.Poll;
import com.example.foruminforexchange.model.Prefix;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreatePostRequest {
    String title;
    Long prefixId;
    Long categoryId;
    String commentContent;
    String pollQuestion;
    Long maximumSelectableResponses;
    boolean isUnlimited;
    boolean changeVote;
    boolean viewResultNoVote;
    List<String> pollResponses;
}
