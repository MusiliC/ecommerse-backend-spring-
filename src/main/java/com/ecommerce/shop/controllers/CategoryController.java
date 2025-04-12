package com.ecommerce.shop.controllers;

import com.ecommerce.shop.config.AppConstants;
import com.ecommerce.shop.models.Category;
import com.ecommerce.shop.payload.CategoryDto;
import com.ecommerce.shop.payload.CategoryResponse;
import com.ecommerce.shop.response.ApiResponse;
import com.ecommerce.shop.service.CategoryServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class CategoryController {

    private final CategoryServiceI categoryServiceI;

    @GetMapping("/public/categories")
    public ResponseEntity<ApiResponse> getCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortOrder
    ) {
        CategoryResponse categoryList = categoryServiceI.getCategories(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(new ApiResponse(true, categoryList));
    }

    @PostMapping("/public/categories")
    public ResponseEntity<ApiResponse> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto savedCategoryDto = categoryServiceI.createCategory(categoryDto);
        return ResponseEntity.ok(new ApiResponse(true, savedCategoryDto));
    }

    @DeleteMapping("/public/categories/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long categoryId) {
        String status = categoryServiceI.deleteCategory(categoryId);
        return ResponseEntity.ok(new ApiResponse(true, status));
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable Long categoryId) {

        CategoryDto savedCategoryDto = categoryServiceI.updateCategory(categoryDto, categoryId);
        return ResponseEntity.ok(new ApiResponse(true, savedCategoryDto));
    }
}
