package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.dto.CategoryDto;
import com.example.foruminforexchange.dto.CreateCategoryRequest;
import com.example.foruminforexchange.dto.EditCategoryRequest;
import com.example.foruminforexchange.mapper.PostMapper;
import com.example.foruminforexchange.model.Category;
import com.example.foruminforexchange.model.Topic;
import com.example.foruminforexchange.repository.CategoryRepo;
import com.example.foruminforexchange.repository.TopicRepo;
import com.example.foruminforexchange.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    private final TopicRepo topicRepo;

    @Override
    public Page<CategoryDto> getAllCategory(Pageable pageable) {
        if (pageable == null || pageable.getPageSize() <= 0) {
            pageable = PageRequest.of(0, 10, Sort.by("categoryId").ascending());
        } else {
            int pageSize = Math.min(pageable.getPageSize(), 10);
            pageable = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort().and(Sort.by("categoryId").ascending()));
        }
        Page<Category> categories = categoryRepo.findAll(pageable);
        if(categories == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        Page<CategoryDto> categoryDtos = categories.map(category -> PostMapper.convertCategoryToDto(category));
        return categoryDtos;
    }

    @Override
    public CategoryDto createCategory(CreateCategoryRequest createCategoryRequest) {
        if(createCategoryRequest.getCategoryName() == null || createCategoryRequest.getTopicId() == null)
            throw new AppException(ErrorCode.NOT_BLANK);
        Topic topic = topicRepo.findById(createCategoryRequest.getTopicId()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_TOPIC));
        Category category = new Category();
        category.setCategoryName(createCategoryRequest.getCategoryName());
        category.setTopic(topic);
        Category savedCategory = categoryRepo.save(category);
        CategoryDto categoryDto = PostMapper.convertCategoryToDto(savedCategory);
        return categoryDto;
    }

    @Override
    public CategoryDto editCategory(EditCategoryRequest editCategoryRequest) {
        if(editCategoryRequest.getCategoryName() == null || editCategoryRequest.getTopicId() == null || editCategoryRequest.getCategoryId() == null)
            throw new AppException(ErrorCode.NOT_BLANK);
        Category category = categoryRepo.findById(editCategoryRequest.getCategoryId()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        Topic topic = topicRepo.findById(editCategoryRequest.getTopicId()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_TOPIC));
        category.setCategoryName(editCategoryRequest.getCategoryName());
        category.setTopic(topic);
        Category savedCategory = categoryRepo.save(category);
        CategoryDto categoryDto = PostMapper.convertCategoryToDto(savedCategory);
        return categoryDto;
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        categoryRepo.delete(category);
        return "Delete category successfully!";
    }
}
