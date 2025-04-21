package com.ecommerce.shop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

    private Long cartItemId;
    private CartDto cart;
    private Double productPrice;
    private Double discount;
    private ProductDto productDto;
    private Integer quantity;
}
