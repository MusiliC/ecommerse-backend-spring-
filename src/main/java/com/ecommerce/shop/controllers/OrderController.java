package com.ecommerce.shop.controllers;


import com.ecommerce.shop.dtos.OrderDto;
import com.ecommerce.shop.dtos.OrderRequestDto;
import com.ecommerce.shop.response.ApiResponse;
import com.ecommerce.shop.service.OrderServiceI;
import com.ecommerce.shop.util.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final OrderServiceI orderService;

    private final AuthUtil authUtil;

    @PostMapping("users/payments/{paymentMethod}")
    public ResponseEntity<ApiResponse> orderProducts(@PathVariable String paymentMethod, @Valid @RequestBody OrderRequestDto orderRequestDto) {
        String emailId = authUtil.loggedInEmail();

        OrderDto order = orderService.placeOrder(emailId, orderRequestDto.getAddressId(), paymentMethod, orderRequestDto.getPgName(), orderRequestDto.getPgPaymentId(), orderRequestDto.getPgStatus(), orderRequestDto.getPgResponseMessage());

        return new ResponseEntity<>(
                new ApiResponse(true, order),
                HttpStatus.CREATED
        );
    }
}
