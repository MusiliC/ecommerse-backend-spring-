package com.ecommerce.shop.dtos;


import com.ecommerce.shop.models.Address;
import com.ecommerce.shop.models.OrderItem;
import com.ecommerce.shop.models.OrderStatus;
import com.ecommerce.shop.models.Payment;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long orderId;

    private String email;

    private List<OrderItemDto> orderItems;

    private LocalDateTime orderDate;

    private PaymentDto payment;

    private OrderStatus orderStatus;

    private Long addressId;
}
