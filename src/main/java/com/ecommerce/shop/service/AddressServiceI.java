package com.ecommerce.shop.service;

import com.ecommerce.shop.dtos.AddressDto;
import com.ecommerce.shop.dtos.AddressResponse;
import com.ecommerce.shop.models.User;
import java.util.List;

public interface AddressServiceI {

    AddressDto addAddress(AddressDto addressDto, User user);

    AddressResponse getAddresses();

    AddressDto getSIngleAddressById(Long addressId);

    AddressResponse getAddressesByUser(User user);

    AddressDto updateAddress(Long addressId, AddressDto addressDto);

    String deleteAddress(Long addressId);
}
