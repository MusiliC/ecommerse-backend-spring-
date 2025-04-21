package com.ecommerce.shop.service;


import com.ecommerce.shop.dtos.CartDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    CartDto addProductToCart(Long productId, Integer quantity);

    List<CartDto> getCarts();
}
