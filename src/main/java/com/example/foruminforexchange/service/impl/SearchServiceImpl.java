package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Specifications.PostSpecification;
import com.example.foruminforexchange.dto.PostDto;
import com.example.foruminforexchange.mapper.PostMapper;
import com.example.foruminforexchange.model.Post;
import com.example.foruminforexchange.repository.PostRepo;
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

    @Override
    @Transactional
    public Page<PostDto> filterPost(Long prefixId, String searchKeyword, Long updateTime, String postType, String sortField, String sortOrder, Pageable pageable) {

        Specification<Post> spec = Specification.where(null);
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
        Page<PostDto> dtoPost = result.map(res -> PostMapper.convertToPostDto(res));
        return dtoPost;
    }

}
