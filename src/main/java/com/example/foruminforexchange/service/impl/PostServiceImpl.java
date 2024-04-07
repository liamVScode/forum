package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.configuration.SecurityUtil;
import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.mapper.PostMapper;
import com.example.foruminforexchange.mapper.UserMapper;
import com.example.foruminforexchange.model.*;
import com.example.foruminforexchange.repository.*;
import com.example.foruminforexchange.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.yaml.snakeyaml.tokens.CommentToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    @Autowired
    private final PostRepo postRepo;
    @Autowired
    private final PollRepo pollRepo;
    @Autowired
    private final CommentRepo commentRepo;
    @Autowired
    private final UserRepo userRepo;
    @Autowired
    private final SecurityUtil securityUtil;
    @Autowired
    private final ImageCommentRepo imageCommentRepo;
    @Autowired
    private final PrefixRepo prefixRepo;
    @Autowired
    private final CategoryRepo categoryRepo;
    @Autowired
    private final ResponseRepo responseRepo;
    @Autowired
    private final LikeRepo likeRepo;
    @Autowired
    private final ReportRepo reportRepo;
    @Autowired
    private final ActivityRepo activityRepo;
    @Autowired
    private final TopicRepo topicRepo;
    @Override
    public List<Post> getAllPostsByCategory(Long categoryId){
        List<Post> lstPost = postRepo.findByCategoryCategoryId(categoryId);
        if(lstPost == null){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }
        return lstPost;
    }

    @Override
    public TopicPrefixResponse getPrefixAndTopic(Long categoryId) {
        Category category = categoryRepo.findByCategoryId(categoryId);
        if(category != null){
            Topic topic = category.getTopic();
            if(topic != null){
                List<Prefix> lstPrefix = prefixRepo.findPrefixesByTopicId(topic.getTopicId());
                System.out.println(lstPrefix);
                TopicPrefixResponse topicPrefixResponse = new TopicPrefixResponse();
                topicPrefixResponse.setTopic(PostMapper.convertToTopicDto(topic));
                List<PrefixDto> lstPrefixDto = lstPrefix.stream()
                        .map(PostMapper::convertPrefixToDto)
                        .collect(Collectors.toList());
                topicPrefixResponse.setPrefix(lstPrefixDto);
                return topicPrefixResponse;
            }
        }
        return null;
    }

    @Override
    public PostDto showDetailPost(Long postId){
        Post post = postRepo.findByPostId(postId);
        if(post == null){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }
        post.setViewCount(post.getViewCount() + 1);
        postRepo.save(post);
        return PostMapper.convertToPostDto(post);
    }
    @Override
    public CreatePostResponse createPost(CreatePostRequest createPostRequest){
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        } else {
            Post post = new Post();

            post.setTitle(createPostRequest.getTitle());
            post.setCreateAt(LocalDateTime.now());
            post.setCommentCount(1L);
            post.setUser(userRepo.findUserByEmail(securityUtil.getCurrentUsername()));
            post.setPrefix(prefixRepo.findPrefixByPrefixId(createPostRequest.getPrefixId()));
            post.setCategory(categoryRepo.findByCategoryId(createPostRequest.getCategoryId()));

            postRepo.save(post);

            if(createPostRequest.getCommentContent() != null){
                Comment comment = new Comment();
                comment.setContent(createPostRequest.getCommentContent());
                comment.setUser(userRepo.findUserByEmail(securityUtil.getCurrentUsername()));
                comment.setPost(post);
                commentRepo.save(comment);

                if (createPostRequest.getImageUrls() != null && !createPostRequest.getImageUrls().isEmpty()) {
                    for (String imageUrl : createPostRequest.getImageUrls()) {
                        ImageComment imageComment = new ImageComment(imageUrl, comment);
                        imageCommentRepo.save(imageComment);
                    }
                }
            }

            if (createPostRequest.getPollQuestion() != null){
                handlePoll(post, createPostRequest.getPollQuestion(), createPostRequest.isChangeVote(), createPostRequest.isViewResultNoVote(),
                        createPostRequest.isUnlimited(), createPostRequest.getMaximumSelectableResponses(), createPostRequest.getPollResponses());

            }

            return PostMapper.convertToCreatePostResponse(post);
        }
    }
    @Override
    public EditPostResponse editPost(EditPostRequest editPostRequest){
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        } else {
            if (postRepo.findByPostId(editPostRequest.getPostId()) == null) {
                throw new AppException(ErrorCode.POST_NOT_FOUND);
            }

            Post editPost = postRepo.findByPostId(editPostRequest.getPostId());
            editPost.setTitle(editPostRequest.getTitle());
            editPost.setUpdateAt(LocalDateTime.now());
            editPost.setPrefix(prefixRepo.findPrefixByPrefixId(editPostRequest.getPrefixId()));
            editPost.setCategory(categoryRepo.findByCategoryId(editPostRequest.getCategoryId()));

            postRepo.save(editPost);

            Page<Comment> commentPage = commentRepo.findFirstCommentByPostId(editPost.getPostId(), PageRequest.of(0, 1));
            Comment comment = commentPage.getContent().get(0);
            comment.setContent(editPostRequest.getCommentContent());
            comment.setPost(editPost);
            Comment savedComment = commentRepo.save(comment);

            if (editPostRequest.getImageUrls() != null && !editPostRequest.getImageUrls().isEmpty()) {
                List<ImageComment> imgOld;
                imgOld = imageCommentRepo.findAllByCommentCommentId(savedComment.getCommentId());
                if(imgOld != null){
                    imageCommentRepo.deleteByCommentId(savedComment.getCommentId());
                }

                for (String imageUrl : editPostRequest.getImageUrls()) {
                    ImageComment imageComment = new ImageComment(imageUrl, comment);
                    imageCommentRepo.save(imageComment);
                }
            } else {
                List<ImageComment> imgOld;
                imgOld = imageCommentRepo.findAllByCommentCommentId(savedComment.getCommentId());
                if(imgOld != null){
                    imageCommentRepo.deleteByCommentId(savedComment.getCommentId());
                }
            }

            if (editPostRequest.getPollQuestion() != null) {
                Poll poll = pollRepo.findByPostPostId(editPost.getPostId());
                if(poll != null){
                    poll.setQuestion(editPostRequest.getPollQuestion());
                    poll.setChangeVote(editPostRequest.isChangeVote() ? true : false);
                    poll.setViewResultsNoVote(editPostRequest.isViewResultNoVote() ? true : false);
                    poll.setIsUnlimited(editPostRequest.isUnlimited() ? true : false);
                    poll.setPost(editPost);
                    poll.setMaximumSelectableResponses(editPostRequest.getMaximumSelectableResponses());

                    Poll savedPoll = pollRepo.save(poll);

                    List<Response> responses = responseRepo.findAllByPollPollId(savedPoll.getPollId());
                    for (Response response : responses) {

                        response.setResponseContent(response.getResponseContent());
                        response.setVoteCount(0L);
                        response.setPoll(poll);

                        responseRepo.save(response);
                    }
                }
                else {
                    handlePoll(editPost, editPostRequest.getPollQuestion(), editPostRequest.isChangeVote(), editPostRequest.isViewResultNoVote(),
                            editPostRequest.isUnlimited(), editPostRequest.getMaximumSelectableResponses(), editPostRequest.getPollResponses());
                }
            }
            return PostMapper.convertToEditPostResponse(editPost);
        }
    }

    private void handlePoll(Post post, String pollQuestion, Boolean changeVote, Boolean viewResultsNoVote, Boolean isUnlimited, Long maximumSelectableResponses, List<String> pollResponses) {
        if (pollQuestion != null) {
            Poll poll = new Poll();
            poll.setQuestion(pollQuestion);
            poll.setChangeVote(changeVote);
            poll.setViewResultsNoVote(viewResultsNoVote);
            poll.setIsUnlimited(isUnlimited);
            poll.setPost(post);
            poll.setMaximumSelectableResponses(maximumSelectableResponses);
            pollRepo.save(poll);

            for (String responseContent : pollResponses) {
                Response response = new Response();
                response.setResponseContent(responseContent);
                response.setPoll(poll);
                responseRepo.save(response);
            }
        }
    }
    @Override
    public String deletePost(DeletePostRequest deletePostRequest) {
        if(postRepo.findByPostId(deletePostRequest.getPostId()) == null){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }


        List<Comment> lstComment = commentRepo.findAllByPostPostId(deletePostRequest.getPostId());
        if(lstComment != null){
            for(Comment cmt : lstComment){
                imageCommentRepo.deleteByCommentId(cmt.getCommentId());
            }
            commentRepo.deleteByPostId(deletePostRequest.getPostId());
        }


        Poll poll = pollRepo.findByPostPostId(deletePostRequest.getPostId());
        if(poll != null){
            responseRepo.deleteByPollId(poll.getPollId());
            pollRepo.deleteByPostId(deletePostRequest.getPostId());
        }

        postRepo.delete(postRepo.findByPostId(deletePostRequest.getPostId()));

        return "Delete Succesfully";
    }
    @Override
    public String lockPost(@RequestBody Long postId){
        Post post = postRepo.findByPostId(postId);
        if(post == null){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        post.setIsLocked(true);
        postRepo.save(post);
        return "Post is locked succesfully!";
    }
    @Override
    public String unlockPost(@RequestBody Long postId){
        Post post = postRepo.findByPostId(postId);
        if(post == null){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }
        post.setIsLocked(false);
        postRepo.save(post);
        return "Post is unlocked succesfully!";
    }
    @Override
    public CommentDto createComment(CreateCommentRequest createCommentRequest){
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        } else {

            Post post = postRepo.findByPostId(createCommentRequest.getPostId());
            if (post == null) {
                throw new AppException(ErrorCode.POST_NOT_FOUND);
            }
            User user = userRepo.findUserByEmail(currentUserEmail);

            Comment comment = new Comment();
            comment.setPost(post);
            comment.setUser(user);
            comment.setCreateAt(LocalDateTime.now());
            comment.setContent(createCommentRequest.getContent());

            if (createCommentRequest.getImageUrls() != null && !createCommentRequest.getImageUrls().isEmpty()) {
                for(String imageUrl : createCommentRequest.getImageUrls()){
                    ImageComment imageComment = new ImageComment(imageUrl, comment);

                    imageCommentRepo.save(imageComment);
                }
            }

            post.setCommentCount(post.getCommentCount() + 1);
            postRepo.save(post);

            Activity activity = new Activity();
            activity.setCreatedAt(comment.getCreateAt());
            activity.setPost(post);
            activity.setType("comment");
            activity.setContentComment(comment.getContent());
            activity.setUser(user);
            activityRepo.save(activity);

            Comment savedComment = commentRepo.save(comment);

            return PostMapper.convertToCommentDto(savedComment);
        }
    }

    @Override
    public CommentDto editComment(EditCommentRequest editCommentRequest) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        User user = userRepo.findUserByEmail(currentUserEmail);
        Post post = postRepo.findByPostId(editCommentRequest.getPostId());
        Comment comment = commentRepo.findByCommentIdAndPostPostIdAndUserUserId(editCommentRequest.getCommentId(), editCommentRequest.getPostId(), user.getUserId());
        if(comment == null){
            throw new AppException(ErrorCode.COMMENT_NOT_FOUND);
        }

        comment.setContent(editCommentRequest.getContent());
        comment.setUpdateAt(LocalDateTime.now());


        if (editCommentRequest.getImageUrls() != null && !editCommentRequest.getImageUrls().isEmpty()) {
            List<ImageComment> imgOld;
            imgOld = imageCommentRepo.findAllByCommentCommentId(comment.getCommentId());
            if(imgOld != null){
                imageCommentRepo.deleteByCommentId(comment.getCommentId());
            }

            for (String imageUrl : editCommentRequest.getImageUrls()) {
                ImageComment imageComment = new ImageComment(imageUrl, comment);
                imageCommentRepo.save(imageComment);
            }
        } else {
            List<ImageComment> imgOld;
            imgOld = imageCommentRepo.findAllByCommentCommentId(comment.getCommentId());
            if(imgOld != null){
                imageCommentRepo.deleteByCommentId(comment.getCommentId());
            }
        }
        Comment savedComment = commentRepo.save(comment);
        return PostMapper.convertToCommentDto(savedComment);
    }

    @Override
    public String deleteComment(DeleteCommentRequest deleteCommentRequest) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepo.findUserByEmail(currentUserEmail);
        Comment comment = commentRepo.findByCommentIdAndPostPostIdAndUserUserId(deleteCommentRequest.getCommentId(), deleteCommentRequest.getPostId(), user.getUserId());
        if(comment == null){
            return "Comment not exited";
        }
        commentRepo.delete(comment);
        return "Delete Successfully";
    }

    @Override
    public String likePost(Long postId) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepo.findUserByEmail(currentUserEmail);
        Post post = postRepo.findByPostId(postId);
        if(post == null){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }
        Like liked = likeRepo.findByUserUserIdAndPostPostId(user.getUserId(), postId);
        if(liked != null){
            return "Cannot like second time";
        }
        Like like = new Like();
        like.setCreateAt(LocalDateTime.now());
        like.setPost(post);
        like.setUser(user);
        likeRepo.save(like);

        Activity activity = new Activity();
        activity.setCreatedAt(like.getCreateAt());
        activity.setPost(post);
        activity.setType("like");
        activity.setContent("like");
        activity.setUser(user);
        activityRepo.save(activity);
        post.setLikeCount(post.getLikeCount() + 1);
        postRepo.save(post);
        return "Like succesfully";
    }

    @Override
    public String unlikePost(Long postId) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepo.findUserByEmail(currentUserEmail);
        Post post = postRepo.findByPostId(postId);
        if(post == null){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }
        Like like = likeRepo.findByUserUserIdAndPostPostId(user.getUserId(), postId);
        if(like == null){
            return "You have not liked this post yet";
        }
        likeRepo.delete(like);
        post.setLikeCount(post.getLikeCount() - 1);
        postRepo.save(post);
        return "Unlike succesfully";
    }

    @Override
    public String reportPost(ReportPostRequest reportPostRequest){
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepo.findUserByEmail(currentUserEmail);
        Post post = postRepo.findByPostId(reportPostRequest.getPostId());
        if(post == null){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        Report reported = reportRepo.findByUserUserIdAndPostPostId(user.getUserId(), reportPostRequest.getPostId());
        if(reported != null){
            throw new AppException(ErrorCode.REPORT_EXISTED);
        }

        Report report = new Report();
        report.setCreateAt(LocalDateTime.now());
        report.setReportContent(reportPostRequest.getReportContent());
        report.setUser(user);
        report.setPost(post);

        post.setReportCount(post.getReportCount() + 1);

        postRepo.save(post);
        reportRepo.save(report);

        return "Reported successfully";

    }



}
