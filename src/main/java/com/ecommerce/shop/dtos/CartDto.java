package com.ecommerce.shop.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private Long cartId;
    private Long userId;
    private Double totalPrice;
    private List<ProductDto> products;
}
