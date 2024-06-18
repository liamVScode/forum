package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.PostDto;
import com.example.foruminforexchange.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchService{
    Page<PostDto> filterPost(Long categoryId,
                             Long prefixId,
                             String searchKeyword,
                             Long updateTime,
                             Long postType,
                             Long report,
                             String sortField,
                             String sortOrder,
                             Pageable pageable);

    Page<UserDto> filterUser(String searchKeyword,
                             Long locked,
                             Pageable pageable);
}
