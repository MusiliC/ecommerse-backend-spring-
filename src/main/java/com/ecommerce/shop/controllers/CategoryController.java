package com.ecommerce.shop.controllers;

import com.ecommerce.shop.models.Category;
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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class CategoryController {

   private final CategoryServiceI categoryServiceI;

    @GetMapping("/public/categories")
    public  ResponseEntity<ApiResponse> getCategories(){
        try {
            List<Category> categoryList = categoryServiceI.getCategories();
            return ResponseEntity.ok(new ApiResponse("Success", categoryList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/public/categories")
    public ResponseEntity<ApiResponse> createCategory(@RequestBody Category category) {
        try {
            categoryServiceI.createCategory(category);
            return ResponseEntity.ok(new ApiResponse("Success", category));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/public/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable  Long categoryId) {

        try {
            String status = categoryServiceI.deleteCategory(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PostMapping("/public/categories/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody  Category category, @PathVariable Long categoryId) {

        try {
            Category savedCategory = categoryServiceI.updateCategory(category, categoryId);
            return ResponseEntity.ok(new ApiResponse("Success", savedCategory));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
