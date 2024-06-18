package com.example.foruminforexchange.dto;

import lombok.Data;

import java.util.List;

@Data
public class EditPostRequest {
    Long postId;
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
