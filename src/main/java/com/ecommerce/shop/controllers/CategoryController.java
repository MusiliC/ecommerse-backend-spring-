package com.ecommerce.shop.controllers;

import com.ecommerce.shop.models.Category;
import com.ecommerce.shop.payload.CategoryDto;
import com.ecommerce.shop.payload.CategoryResponse;
import com.ecommerce.shop.response.ApiResponse;
import com.ecommerce.shop.service.CategoryServiceI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class CategoryController {

    private final CategoryServiceI categoryServiceI;

    @GetMapping("/public/categories")
    public ResponseEntity<ApiResponse> getCategories() {
        CategoryResponse categoryList = categoryServiceI.getCategories();
        return ResponseEntity.ok(new ApiResponse("Success", categoryList));
    }

    @PostMapping("/public/categories")
    public ResponseEntity<ApiResponse> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto savedCategoryDto = categoryServiceI.createCategory(categoryDto);
        return ResponseEntity.ok(new ApiResponse("Success", savedCategoryDto));
    }

    @DeleteMapping("/public/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        String status = categoryServiceI.deleteCategory(categoryId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable Long categoryId) {

        CategoryDto savedCategoryDto = categoryServiceI.updateCategory(categoryDto, categoryId);
        return ResponseEntity.ok(new ApiResponse("Success", savedCategoryDto));
    }
}
