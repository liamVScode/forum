package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.CategoryDto;
import com.example.foruminforexchange.dto.CreateCategoryRequest;
import com.example.foruminforexchange.dto.EditCategoryRequest;
import com.example.foruminforexchange.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryDto> getAllCategory(Pageable pageable);
    CategoryDto createCategory(CreateCategoryRequest createCategoryRequest);
    CategoryDto editCategory(EditCategoryRequest editCategoryRequest);
    String deleteCategory(Long categoryId);
}
