package com.ecommerce.shop.repository;

import com.ecommerce.shop.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
