package com.ecommerce.shop.controllers;


import com.ecommerce.shop.dtos.CartDto;
import com.ecommerce.shop.models.Cart;
import com.ecommerce.shop.repository.CartRepository;
import com.ecommerce.shop.response.ApiResponse;
import com.ecommerce.shop.service.CartService;
import com.ecommerce.shop.util.AuthUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private CartRepository cartRepository;

    @PostMapping("products/{productId}/quantity/{quantity}")
    public ResponseEntity<ApiResponse> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity) {
        CartDto cartDto = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(
                new ApiResponse(true, cartDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse> getCarts() {
        List<CartDto> cartDtos = cartService.getCarts();
        return new ResponseEntity<>(
                new ApiResponse(true, cartDtos),
                HttpStatus.OK
        );
    }

    @GetMapping("users/cart")
    public ResponseEntity<ApiResponse> getCartByUserId() {

        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);

        CartDto cartDto = cartService.getCart(emailId, cart.getCartId());

        return new ResponseEntity<>(
                new ApiResponse(true, cartDto),
                HttpStatus.OK
        );
    }

    @PutMapping("products/{productId}/quantity/{operation}")
    public ResponseEntity<ApiResponse> updateProductQuantity(@PathVariable Long productId,
                                                             @PathVariable String operation) {
        CartDto cartDto = cartService.updateProductQuantity(productId, operation.equalsIgnoreCase("delete") ? -1 : 1);
        return new ResponseEntity<>(
                new ApiResponse(true, cartDto),
                HttpStatus.OK
        );
    }

    @DeleteMapping("{cartId}/product/{productId}")
    public ResponseEntity<ApiResponse> deleteProductFromCart(@PathVariable Long cartId,
                                                              @PathVariable Long productId) {
        String status = cartService.deleteProductFromCart(cartId, productId);
        return new ResponseEntity<>(
                new ApiResponse(true, status),
                HttpStatus.OK
        );
    }


}
