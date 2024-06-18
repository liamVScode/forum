package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Specifications.PostSpecification;
import com.example.foruminforexchange.Specifications.UserSpecification;
import com.example.foruminforexchange.dto.PostDto;
import com.example.foruminforexchange.dto.UserDto;
import com.example.foruminforexchange.mapper.PostMapper;
import com.example.foruminforexchange.mapper.UserMapper;
import com.example.foruminforexchange.model.Post;
import com.example.foruminforexchange.model.User;
import com.example.foruminforexchange.repository.PostRepo;
import com.example.foruminforexchange.repository.UserRepo;
import com.example.foruminforexchange.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    @Autowired
    private final PostRepo postRepo;

    @Autowired
    private final UserRepo userRepo;

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    @Transactional
    public Page<PostDto> filterPost(Long categoryId, Long prefixId, String searchKeyword, Long updateTime, Long postType, Long report, String sortField, String sortOrder, Pageable pageable) {

        Specification<Post> spec = Specification.where(null);
        if(categoryId != null){
            spec = spec.and(PostSpecification.hasCategory(categoryId));
        }

        if(prefixId != null){
            spec = spec.and(PostSpecification.hasPrefix(prefixId));
        }
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            spec = spec.and(PostSpecification.hasKeywordInTitleOrFirstComment(searchKeyword));
        }

        if (updateTime != null) {
            spec = spec.and(PostSpecification.updatedWithinDays(updateTime));
        }

        if (postType != null) {
            spec = spec.and(PostSpecification.hasPoll(postType));
        }
        if (report != null) {
            spec = spec.and(PostSpecification.hasReport(report));
        }

        // Kiểm tra và xử lý sortOrder
        if (sortOrder == null || (!sortOrder.equalsIgnoreCase("asc") && !sortOrder.equalsIgnoreCase("desc"))) {
            sortOrder = "asc";
        }

        if (sortField != null && !sortField.isEmpty()) {
            Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        }

        Page<Post> result = postRepo.findAll(spec, pageable);
        Page<PostDto> dtoPost = result.map(res -> postMapper.convertToPostDto(res));
        return dtoPost;
    }

    @Override
    @Transactional
    public Page<UserDto> filterUser(String searchKeyword, Long locked, Pageable pageable){

        Specification<User> spec = Specification.where(null);

        if(searchKeyword != null && !searchKeyword.isEmpty()){
            spec = spec.and(UserSpecification.searchByKeyword(searchKeyword));
        }

        if(locked != null){
            spec = spec.and(UserSpecification.isLocked(locked));
        }

        if (pageable == null || pageable.getPageSize() <= 0) {
            pageable = PageRequest.of(0, 10, Sort.by("userId").descending());
        } else {
            int pageSize = Math.min(pageable.getPageSize(), 10);
            pageable = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort().and(Sort.by("userId").descending()));
        }

        Page<User> result = userRepo.findAll(spec, pageable);
        Page<UserDto> userDtos = result.map(res -> userMapper.convertToUserDto(res));
        return  userDtos;
    }

}
