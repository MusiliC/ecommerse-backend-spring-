package com.ecommerce.shop.service;

import com.ecommerce.shop.models.Category;
import com.ecommerce.shop.payload.CategoryDto;
import com.ecommerce.shop.payload.CategoryResponse;
import java.util.List;

public interface CategoryServiceI {

    CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDto createCategory(CategoryDto category);

    String deleteCategory(Long categoryId);

    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);
}
