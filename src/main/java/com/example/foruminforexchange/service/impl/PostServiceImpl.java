package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.configuration.SecurityUtil;
import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.mapper.PostMapper;
import com.example.foruminforexchange.mapper.PrefixMapper;
import com.example.foruminforexchange.mapper.TopicMapper;
import com.example.foruminforexchange.mapper.UserMapper;
import com.example.foruminforexchange.model.*;
import com.example.foruminforexchange.repository.*;
import com.example.foruminforexchange.service.FileStorageService;
import com.example.foruminforexchange.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.tokens.CommentToken;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final BookmarkRepo bookmarkRepo;


    private final FileStorageService fileStorageService;
    private final ResponseUserRepo responseUserRepo;

    @Override
    public Page<PostDto> getAllPost(Pageable pageable) {
        if (pageable == null || pageable.getPageSize() <= 0) {
            pageable = PageRequest.of(0, 10, Sort.by("createAt").descending());
        } else {
            System.out.println("Tôi là page number: " + pageable.getPageNumber() + " và tôi là page size: " + pageable.getPageSize());
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().and(Sort.by("createAt").descending()));
        }

        Page<Post> lstPost = postRepo.findAll(pageable);
        Page<PostDto> lstPostDto = lstPost.map(
                post -> PostMapper.convertToPostDto(post)
        );

        return lstPostDto;
    }

    @Override
    public Page<PostDto> getPostByReport(Pageable pageable) {
        if (pageable == null || pageable.getPageSize() <= 0) {
            pageable = PageRequest.of(0, 10, Sort.by("createAt").descending());
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().and(Sort.by("createAt").descending()));
        }
        Page<Post> lstPostRp = postRepo.findByReportCountGreaterThan(0L, pageable);
        if(lstPostRp == null){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }
        Page<PostDto> lstPostDto = lstPostRp.map(
                post -> PostMapper.convertToPostDto(post)
        );
        return lstPostDto;
    }

    @Override
    public Page<PostDto> getAllPostsByCategory(Long categoryId, Pageable pageable){
        if (pageable == null || pageable.getPageSize() <= 0) {
            pageable = PageRequest.of(0, 10, Sort.by("createAt").descending());
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().and(Sort.by("createAt").descending()));
        }

        Page<Post> lstPost = postRepo.findByCategoryCategoryId(categoryId, pageable);
        if(lstPost == null){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        Page<PostDto> lstPostDto = lstPost.map(
                post -> PostMapper.convertToPostDto(post)
        );

        return lstPostDto;
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
                topicPrefixResponse.setTopic(TopicMapper.convertToTopicDto(topic));
                List<PrefixDto> lstPrefixDto = lstPrefix.stream()
                        .map(PrefixMapper::convertToPrefixDto)
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
    @Transactional
    @Override
    public CreatePostResponse createPost(CreatePostRequest createPostRequest, List<MultipartFile> imageFiles){
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

            Comment comment = new Comment();
            comment.setContent(createPostRequest.getCommentContent());
            comment.setUser(userRepo.findUserByEmail(securityUtil.getCurrentUsername()));
            comment.setPost(post);
            commentRepo.save(comment);

            if(imageFiles != null && !imageFiles.isEmpty()){
                List<String> imageUrls = new ArrayList<>();
                for (MultipartFile file : imageFiles) {
                    String imageUrl = fileStorageService.storeFile(file);
                    imageUrls.add(imageUrl);
                    ImageComment imageComment = new ImageComment(imageUrl, comment);
                    imageCommentRepo.save(imageComment);
                }
            }

            if (createPostRequest.getPollQuestion() != null){
                handlePoll(post, createPostRequest.getPollQuestion(), createPostRequest.isChangeVote(), createPostRequest.isViewResultNoVote(),
                        createPostRequest.isUnlimited(), createPostRequest.getMaximumSelectableResponses(), createPostRequest.getPollResponses());

            }

            return PostMapper.convertToCreatePostResponse(post);
        }
    }
    @Transactional
    @Override
    public EditPostResponse editPost(EditPostRequest editPostRequest, List<MultipartFile> imageFiles){
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

            // Xóa hình ảnh cũ từ cơ sở dữ liệu và lưu trữ vật lý
            List<ImageComment> existingImages = imageCommentRepo.findAllByCommentCommentId(savedComment.getCommentId());
            for (ImageComment image : existingImages) {
                fileStorageService.deleteFile(image.getImageUrl());
                imageCommentRepo.delete(image);
            }

            // Lưu hình ảnh mới và cập nhật cơ sở dữ liệu
            if (imageFiles != null && !imageFiles.isEmpty()) {
                for (MultipartFile file : imageFiles) {
                    String imageUrl = fileStorageService.storeFile(file);
                    ImageComment newImageComment = new ImageComment(imageUrl, savedComment);
                    imageCommentRepo.save(newImageComment);
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
    @Transactional
    @Override
    public String deletePost(Long postId) {
        Post post = postRepo.findByPostId(postId);
        if(post == null){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        List<Comment> lstComment = commentRepo.findAllByPostPostId(postId);
        for(Comment cmt : lstComment){
            if(cmt.getImages() != null){
                imageCommentRepo.deleteByCommentId(cmt.getCommentId());
            }
            if(cmt.getLikes() != null){
                likeRepo.deleteByCommentCommentId(cmt.getCommentId());
            }
        }

        if (!lstComment.isEmpty()) {
            commentRepo.deleteByPostId(postId);
        }

        Poll poll = pollRepo.findByPostPostId(postId);
        if (poll != null) {
            List<Response> responses = responseRepo.findAllByPollPollId(poll.getPollId());
            if(responses != null)
                for (Response response : responses) {
                    responseUserRepo.deleteByResponseId(response.getResponseId());
                }
            responseRepo.deleteByPollId(poll.getPollId());
            pollRepo.delete(poll);
        }

        List<Report> reports = reportRepo.findAllByPostPostId(postId);
        if(reports != null){
            reportRepo.deleteAllByPostPostId(postId);
        }

        List<Activity> activities = activityRepo.findAllByPostPostId(postId);
        if(activities != null){
            activityRepo.deleteAllByPostPostId(postId);
        }

        List<Bookmark> bookmarks = bookmarkRepo.findByPostPostId(post.getPostId());
        if(bookmarks != null){
            bookmarkRepo.deleteAllByPostPostId(postId);
        }
        postRepo.delete(post);

        return "Delete Successfully";
    }

    @Override
    public String lockPost(Long postId){
        Post post = postRepo.findByPostId(postId);
        if(post == null){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        post.setIsLocked(true);
        post.setLockedBy(currentUserEmail);
        postRepo.save(post);

        return "Post is locked succesfully!";
    }
    @Override
    public String unlockPost(Long postId){
        Post post = postRepo.findByPostId(postId);
        if(post == null){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        User user = userRepo.findUserByEmail(currentUserEmail);

        if(currentUserEmail == post.getLockedBy() || user.getRole() == Role.ADMIN){
            post.setIsLocked(false);
            post.setLockedBy(null);
            postRepo.save(post);
        }

        return "Post is unlocked succesfully!";
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
