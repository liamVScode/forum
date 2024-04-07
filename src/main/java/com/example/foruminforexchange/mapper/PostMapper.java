package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.model.*;

import java.util.List;
import java.util.stream.Collectors;

public class PostMapper {

    public static CommentDto convertToCommentDto(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentId(comment.getCommentId());
        commentDto.setPostId(comment.getPost().getPostId());
        commentDto.setCreateAt(comment.getCreateAt());
        commentDto.setContent(comment.getContent());
        if (comment.getImages() != null) {
            List<String> imageUrls = comment.getImages().stream()
                    .map(ImageComment::getImageUrl)
                    .collect(Collectors.toList());
            commentDto.setImageUrls(imageUrls);
        }

        commentDto.setUpdateAt(comment.getUpdateAt());
        commentDto.setUser(UserMapper.convertToUserDto(comment.getUser()));

        return commentDto;
    }

    public static CreatePostResponse convertToCreatePostResponse(Post post){
        CreatePostResponse createPostResponse = new CreatePostResponse();
        createPostResponse.setPostId(post.getPostId());
        createPostResponse.setTitle(post.getTitle());
        return createPostResponse;
    }

    public static EditPostResponse convertToEditPostResponse(Post post){
        EditPostResponse editPostResponse = new EditPostResponse();
        editPostResponse.setPostId(post.getPostId());
        editPostResponse.setTitle(post.getTitle());
        return editPostResponse;
    }

    public static PostDto convertToPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setPostId(post.getPostId());
        postDto.setTitle(post.getTitle());
        postDto.setCreateAt(post.getCreateAt());
        postDto.setUpdateAt(post.getUpdateAt());
        postDto.setViewCount(post.getViewCount());
        postDto.setLikeCount(post.getLikeCount());
        postDto.setCommentCount(post.getCommentCount());
        postDto.setShareCount(post.getShareCount());
        postDto.setReportCount(post.getReportCount());
        postDto.setLocked(post.isLocked());
        postDto.setUser(UserMapper.convertToUserDto(post.getUser()));
        if(post.getPrefix() != null){
        postDto.setPrefix(convertPrefixToDto(post.getPrefix()));
        }
        postDto.setCategory(convertCategoryToDto(post.getCategory()));
        if (post.getPoll() != null) {
            postDto.setPoll(convertToPollDto(post.getPoll()));
        }
        List<CommentDto> commentDtos = post.getComments().stream()
                .map(comment -> convertToCommentDto(comment))
                .collect(Collectors.toList());
        postDto.setCommentDto(commentDtos);
        return postDto;
    }

    public static CategoryDto convertCategoryToDto(Category category) {
        CategoryDto prefixDto = new CategoryDto();
        prefixDto.setCategoryId(category.getCategoryId());
        prefixDto.setCategoryName(category.getCategoryName());
        return prefixDto;
    }

    public static PollDto convertToPollDto(Poll poll) {
        PollDto pollDto = new PollDto();
        pollDto.setPollId(poll.getPollId());
        pollDto.setQuestion(poll.getQuestion());
        pollDto.setMaximumSelectableResponses(poll.getMaximumSelectableResponses());
        pollDto.setIsUnlimited(poll.getIsUnlimited());
        pollDto.setChangeVote(poll.getChangeVote());
        pollDto.setViewResultsNoVote(poll.getViewResultsNoVote());

        List<ResponseDto> responseDtos = poll.getResponses().stream()
                .map(response -> convertToResponseDto(response))
                .collect(Collectors.toList());
        pollDto.setResponses(responseDtos);
        return pollDto;
    }

    public static ResponseDto convertToResponseDto(Response response) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResponseContent(response.getResponseContent());
        responseDto.setVoteCount(response.getVoteCount());
        return responseDto;
    }

    public static PrefixDto convertPrefixToDto(Prefix prefix) {
        PrefixDto prefixDto = new PrefixDto();
        prefixDto.setPrefixId(prefix.getPrefixId());
        prefixDto.setPrefixName(prefix.getPrefixName());
        return prefixDto;
    }

    public static TopicDto convertToTopicDto(Topic topic){
        TopicDto topicDto = new TopicDto();
        topicDto.setTopicId(topic.getTopicId());
        topicDto.setTopicName(topic.getTopicName());
        return topicDto;
    }
}