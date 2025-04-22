package com.ecommerce.shop.repository;

import com.ecommerce.shop.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
