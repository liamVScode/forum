package com.example.foruminforexchange.mapper;


import com.example.foruminforexchange.dto.CategoryDto;
import com.example.foruminforexchange.model.Category;

public class CategoryMapper {
    public static CategoryDto convertToCategoryDto(Category category){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryId(category.getCategoryId());
        categoryDto.setCategoryName(category.getCategoryName());
        categoryDto.setTopicDto(TopicMapper.convertToTopicDto(category.getTopic()));
        return categoryDto;
    }
}
