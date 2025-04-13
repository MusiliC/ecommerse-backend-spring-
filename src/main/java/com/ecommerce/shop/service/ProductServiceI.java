package com.ecommerce.shop.service;

import com.ecommerce.shop.payload.ProductDto;
import com.ecommerce.shop.payload.ProductResponse;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ProductServiceI {
    ProductDto addProduct(ProductDto productDto, Long categoryId);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDto updateProduct(Long id, ProductDto productDt0);

    String deleteProduct(Long id);

    ProductDto uploadProductImage(Long productId, MultipartFile image) throws IOException;
}
