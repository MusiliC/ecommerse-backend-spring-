package com.ecommerce.shop.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResponse {
    private Boolean status;
    private Object data;
}
