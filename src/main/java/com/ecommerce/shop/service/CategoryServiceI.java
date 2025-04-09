package com.ecommerce.shop.service;

import com.ecommerce.shop.models.Category;
import java.util.List;

public interface CategoryServiceI {

    List<Category> getCategories();

    void createCategory(Category category);

    String deleteCategory(Long categoryId);

    Category updateCategory(Category category, Long categoryId);
}
