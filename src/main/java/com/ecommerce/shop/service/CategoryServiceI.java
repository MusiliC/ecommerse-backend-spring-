package com.ecommerce.shop.service;

import com.ecommerce.shop.dtos.CategoryDto;
import com.ecommerce.shop.dtos.CategoryResponse;

public interface CategoryServiceI {

    CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDto createCategory(CategoryDto category);

    String deleteCategory(Long categoryId);

    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);
}
