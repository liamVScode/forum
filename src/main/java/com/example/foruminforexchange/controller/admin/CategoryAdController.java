package com.example.foruminforexchange.controller.admin;

import com.example.foruminforexchange.dto.ApiResponse;
import com.example.foruminforexchange.dto.CategoryDto;
import com.example.foruminforexchange.dto.CreateCategoryRequest;
import com.example.foruminforexchange.dto.EditCategoryRequest;
import com.example.foruminforexchange.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/categories")
public class CategoryAdController {

    private final CategoryService categoryService;

    @GetMapping("/all-category")
    public ApiResponse<Page<CategoryDto>> getAllCategory(Pageable pageable){
        ApiResponse<Page<CategoryDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(categoryService.getAllCategory(pageable));
        return apiResponse;
    }

    @PostMapping("/create-category")
    public ApiResponse<CategoryDto> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest){
        ApiResponse<CategoryDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(categoryService.createCategory(createCategoryRequest));
        return apiResponse;
    }

    @PutMapping("/edit-category")
    public ApiResponse<CategoryDto> editCategory(@RequestBody EditCategoryRequest editCategoryRequest){
        ApiResponse<CategoryDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(categoryService.editCategory(editCategoryRequest));
        return apiResponse;
    }

    @DeleteMapping("/delete-category")
    public ApiResponse<String> deleteCategory(@RequestParam("categoryId") Long categoryId){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(categoryService.deleteCategory(categoryId));
        return apiResponse;
    }
}
