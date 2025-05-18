package com.ecommerce.shop.service;


import com.ecommerce.shop.dtos.CartDto;
import com.ecommerce.shop.dtos.CartItemDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    CartDto addProductToCart(Long productId, Integer quantity);

    List<CartDto> getCarts();

    CartDto getCart(String emailId, Long cartId);

    CartDto updateProductQuantity(Long productId, int quantity);

    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductsInCart(Long cartId, Long productId);

    String createOrUpdateCart(List<CartItemDto> cartItems);
}
