package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.dto.CategoryForumInfo;
import com.example.foruminforexchange.dto.ForumInforResponse;
import com.example.foruminforexchange.mapper.CategoryMapper;
import com.example.foruminforexchange.mapper.PostMapper;
import com.example.foruminforexchange.mapper.TopicMapper;
import com.example.foruminforexchange.model.Category;
import com.example.foruminforexchange.model.Post;
import com.example.foruminforexchange.model.Topic;
import com.example.foruminforexchange.repository.CategoryRepo;
import com.example.foruminforexchange.repository.CommentRepo;
import com.example.foruminforexchange.repository.PostRepo;
import com.example.foruminforexchange.repository.TopicRepo;
import com.example.foruminforexchange.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumServiceImpl implements ForumService {
    @Autowired
    private TopicRepo topicRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private CommentRepo commentRepo;

    @Override
    public List<ForumInforResponse> getForumInfor() {
        List<ForumInforResponse> forumInforResponses = new ArrayList<>();
        List<Topic> topics = topicRepo.findAll();

        for (Topic topic : topics) {
            ForumInforResponse forumInforResponse = new ForumInforResponse();
            forumInforResponse.setTopicDto(TopicMapper.convertToTopicDto(topic));

            List<Category> categories = categoryRepo.findAllByTopicTopicId(topic.getTopicId());
            List<CategoryForumInfo> categoryForumInfos = new ArrayList<>();

            for (Category category : categories) {
                CategoryForumInfo categoryForumInfo = new CategoryForumInfo();
                categoryForumInfo.setCategoryDto(CategoryMapper.convertToCategoryDto(category));

                Long numberOfPost = postRepo.countAllByCategoryCategoryId(category.getCategoryId());
                categoryForumInfo.setNumberOfPost(numberOfPost);

                // Tính toán tổng số lượng bình luận
                Long totalComment = calculateTotalComment(category);
                categoryForumInfo.setNumberOfComment(totalComment);

                // Lấy bài viết mới nhất
                Post latestPost = getLatestPost(category);
                if(latestPost != null){
                    categoryForumInfo.setPostDto(PostMapper.convertToPostDto(latestPost));
                }

                categoryForumInfos.add(categoryForumInfo);
            }

            forumInforResponse.setCategoryForumInfos(categoryForumInfos);
            forumInforResponses.add(forumInforResponse);
        }
        return forumInforResponses;
    }

    // Phương thức tính tổng số lượng bình luận của tất cả bài viết trong một danh mục
    private Long calculateTotalComment(Category category) {
        List<Post> posts = postRepo.findAllByCategoryCategoryId(category.getCategoryId());
        Long totalComment = 0L;

        if (posts != null) {
            for (Post post : posts) {
                totalComment += commentRepo.countByPostId(post.getPostId());
            }
        }
        return totalComment;
    }

    // Phương thức lấy bài viết mới nhất từ một danh mục
    private Post getLatestPost(Category category) {
        List<Post> posts = postRepo.findAllByCategoryCategoryId(category.getCategoryId());
        Post latestPost = null;

        if (posts != null) {
            for (Post post : posts) {
                if (latestPost == null || post.getCreateAt().isAfter(latestPost.getCreateAt())) {
                    latestPost = post;
                }
            }
        }

        return latestPost;
    }

}
