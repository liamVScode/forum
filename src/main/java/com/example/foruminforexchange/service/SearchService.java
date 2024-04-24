package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchService{
    Page<PostDto> filterPost(Long prefixId,
                             String searchKeyword,
                             Long updateTime,
                             String postType,
                             String sortField,
                             String sortOrder,
                             Pageable pageable);
}
