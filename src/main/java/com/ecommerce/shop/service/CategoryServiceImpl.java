package com.ecommerce.shop.service;

import com.ecommerce.shop.exceptions.APIException;
import com.ecommerce.shop.exceptions.ResourceNotFoundException;
import com.ecommerce.shop.models.Category;
import com.ecommerce.shop.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryServiceI {

    private final CategoryRepository categoryRepository;


    @Override
    public List<Category> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new APIException("No categories found");
        }
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        Optional<Category> existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(existingCategory.isPresent()) {
            throw new APIException( "Category with the Name " + category.getCategoryName() + "  already exists");
        }
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        categoryRepository.delete(category);

        return "Category deleted successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        existingCategory.setCategoryName(category.getCategoryName());
        return categoryRepository.save(existingCategory);
    }
}
