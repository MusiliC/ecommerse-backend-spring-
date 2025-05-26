package com.ecommerce.shop.dtos;


import lombok.Data;

@Data
public class StripePaymentDto {

    private Long amount;

    private String currency;
}
