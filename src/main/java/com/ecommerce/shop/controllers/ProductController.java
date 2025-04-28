package com.ecommerce.shop.controllers;

import com.ecommerce.shop.config.AppConstants;
import com.ecommerce.shop.dtos.ProductDto;
import com.ecommerce.shop.dtos.ProductResponse;
import com.ecommerce.shop.response.ApiResponse;
import com.ecommerce.shop.service.ProductServiceI;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class ProductController {

    private final ProductServiceI productService;

    @PostMapping("/admin/categories/{categoryId}/products")
    public ResponseEntity<ApiResponse> addProduct(@Valid @RequestBody ProductDto productDtoReq, @PathVariable Long categoryId) {
        ProductDto productDto = productService.addProduct(productDtoReq, categoryId);
        return new ResponseEntity<>(
                new ApiResponse(true, productDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/public/products")
    public ResponseEntity<ApiResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortOrder,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "category", required = false) String category
    ) {
        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder, keyword, category);
        return new ResponseEntity<>(
                new ApiResponse(true, productResponse),
                HttpStatus.OK
        );
    }

    @GetMapping("/admin/categories/{categoryId}/products")
    public ResponseEntity<ApiResponse> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortOrder
    ) {
        ProductResponse productResponse = productService.getProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(
                new ApiResponse(true, productResponse),
                HttpStatus.OK
        );
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ApiResponse> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortOrder
    ) {
        ProductResponse productResponse = productService.getProductsByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(
                new ApiResponse(true, productResponse),
                HttpStatus.OK
        );
    }

    @PutMapping("/admin/products/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id,@Valid @RequestBody ProductDto productReqDto) {
        ProductDto productDto = productService.updateProduct(id, productReqDto);
        return new ResponseEntity<>(
                new ApiResponse(true, productDto),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        String productDto = productService.deleteProduct(id);
        return new ResponseEntity<>(
                new ApiResponse(true, productDto),
                HttpStatus.OK
        );
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ApiResponse> uploadProductImage(@PathVariable Long productId, @RequestParam("image")MultipartFile image) throws IOException {
        ProductDto product = productService.uploadProductImage(productId, image);
        return new ResponseEntity<>(
                new ApiResponse(true, product),
                HttpStatus.OK
        );
    }
}
