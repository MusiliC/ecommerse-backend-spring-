package com.ecommerce.shop.service;

import com.ecommerce.shop.payload.ProductDto;
import com.ecommerce.shop.payload.ProductResponse;

public interface ProductServiceI {
    ProductDto addProduct(ProductDto productDto, Long categoryId);

    ProductResponse getAllProducts();

    ProductResponse getProductsByCategory(Long categoryId);

    ProductResponse getProductsByKeyword(String keyword);

    ProductDto updateProduct(Long id, ProductDto productDt0);

    String deleteProduct(Long id);
}
