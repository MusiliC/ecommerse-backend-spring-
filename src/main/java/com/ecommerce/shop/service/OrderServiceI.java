package com.ecommerce.shop.service;

import com.ecommerce.shop.dtos.OrderDto;

public interface OrderServiceI {
    OrderDto placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}
