package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.model.*;
import com.example.foruminforexchange.repository.CommentRepo;
import com.example.foruminforexchange.repository.LikeRepo;
import com.example.foruminforexchange.repository.PostRepo;
import com.example.foruminforexchange.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostMapper {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private LikeRepo likeRepo;

    @Autowired
    private PostRepo postRepo;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ReportMapper reportMapper;

    public static CreatePostResponse convertToCreatePostResponse(Post post){
        CreatePostResponse createPostResponse = new CreatePostResponse();
        createPostResponse.setPostId(post.getPostId());
        createPostResponse.setTitle(post.getTitle());
        createPostResponse.setCategoryDto(CategoryMapper.convertToCategoryDto(post.getCategory()));
        return createPostResponse;
    }

    public static EditPostResponse convertToEditPostResponse(Post post){
        EditPostResponse editPostResponse = new EditPostResponse();
        editPostResponse.setPostId(post.getPostId());
        editPostResponse.setTitle(post.getTitle());
        editPostResponse.setCategoryDto(CategoryMapper.convertToCategoryDto(post.getCategory()));
        return editPostResponse;
    }

    public PostDto convertToPostDto(Post post) {
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
        postDto.setUser(userMapper.convertToUserDto(post.getUser()));
        if(post.getPrefix() != null){
            postDto.setPrefix(PrefixMapper.convertToPrefixDto(post.getPrefix()));
        }
        postDto.setCategory(CategoryMapper.convertToCategoryDto(post.getCategory()));
        if (post.getPoll() != null) {
            postDto.setPoll(convertToPollDto(post.getPoll()));
        }
        if(post.getReport() != null){
            List<ReportDto> reportDtos = post.getReport().stream()
                    .map(report -> reportMapper.convertToReportDto(report))
                    .collect(Collectors.toList());
            postDto.setReportDto(reportDtos);
        }else postDto.setReportDto(null);

        List<CommentDto> commentDtos = post.getComments().stream()
                .map(comment -> commentMapper.convertToCommentDto(comment))
                .collect(Collectors.toList());
        postDto.setCommentDto(commentDtos);
        return postDto;
    }

    public static PollDto convertToPollDto(Poll poll) {
        PollDto pollDto = new PollDto();
        pollDto.setPollId(poll.getPollId());
        pollDto.setQuestion(poll.getQuestion());
        pollDto.setMaximumSelectableResponses(poll.getMaximumSelectableResponses());
        pollDto.setIsUnlimited(poll.getIsUnlimited());
        pollDto.setChangeVote(poll.getChangeVote());
        pollDto.setViewResultsNoVote(poll.getViewResultsNoVote());
        pollDto.setTimeVote(poll.getTimeVote());
        pollDto.setCreateAt(poll.getCreateAt());
        long totalVotes = poll.getResponses().stream()
                .mapToLong(Response::getVoteCount)
                .sum();

        List<ResponseDto> responseDtos = poll.getResponses().stream()
                .map(response -> {
                    double percentage = 0.0;
                    if (response.getVoteCount() != null && response.getVoteCount() > 0 && totalVotes > 0) {
                        percentage = (double) response.getVoteCount() * 100 / totalVotes;
                    }
                    return ResponseMapper.convertToResponseDto(response, percentage);
                }).collect(Collectors.toList());
        pollDto.setResponses(responseDtos);
        return pollDto;
    }

}
