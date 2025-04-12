package com.ecommerce.shop.controllers;

import com.ecommerce.shop.models.Product;
import com.ecommerce.shop.payload.ProductDto;
import com.ecommerce.shop.payload.ProductResponse;
import com.ecommerce.shop.response.ApiResponse;
import com.ecommerce.shop.service.ProductServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class ProductController {

    private final ProductServiceI productService;

    @PostMapping("/admin/categories/{categoryId}/products")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody Product product, @PathVariable Long categoryId) {
        ProductDto productDto = productService.addProduct(product, categoryId);
        return new ResponseEntity<>(
                new ApiResponse(true, productDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/public/products")
    public ResponseEntity<ApiResponse> getAllProducts() {
        ProductResponse productResponse = productService.getAllProducts();
        return new ResponseEntity<>(
                new ApiResponse(true, productResponse),
                HttpStatus.OK
        );
    }

    @GetMapping("/admin/categories/{categoryId}/products")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable Long categoryId) {
        ProductResponse productResponse = productService.getProductsByCategory(categoryId);
        return new ResponseEntity<>(
                new ApiResponse(true, productResponse),
                HttpStatus.OK
        );
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ApiResponse> getProductsByKeyword(@PathVariable String keyword) {
        ProductResponse productResponse = productService.getProductsByKeyword(keyword);
        return new ResponseEntity<>(
                new ApiResponse(true, productResponse),
                HttpStatus.OK
        );
    }
}
