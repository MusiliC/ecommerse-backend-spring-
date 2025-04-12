package com.ecommerce.shop.service;

import com.ecommerce.shop.models.Product;
import com.ecommerce.shop.payload.ProductDto;
import com.ecommerce.shop.payload.ProductResponse;

public interface ProductServiceI {
    ProductDto addProduct(Product product, Long categoryId);

    ProductResponse getAllProducts();

    ProductResponse getProductsByCategory(Long categoryId);

    ProductResponse getProductsByKeyword(String keyword);
}
