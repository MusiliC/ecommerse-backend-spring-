package com.ecommerce.shop.controllers;


import com.ecommerce.shop.dtos.CartDto;
import com.ecommerce.shop.response.ApiResponse;
import com.ecommerce.shop.service.CartService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("cart/products/{productId}/quantity/{quantity}")
    public ResponseEntity<ApiResponse> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity) {
        CartDto cartDto = cartService.addProductToCart(productId, quantity);
        return  new ResponseEntity<>(
                new ApiResponse(true, cartDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("carts")
    public ResponseEntity<ApiResponse> getCarts(){
        List<CartDto> cartDtos = cartService.getCarts();
        return new ResponseEntity<>(
                new ApiResponse(true, cartDtos),
                HttpStatus.OK
        );
    }
}
