package com.ecommerce.shop.controllers;


import com.ecommerce.shop.dtos.AddressDto;
import com.ecommerce.shop.dtos.AddressResponse;
import com.ecommerce.shop.models.User;
import com.ecommerce.shop.response.ApiResponse;
import com.ecommerce.shop.service.AddressServiceI;
import com.ecommerce.shop.util.AuthUtil;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/addresses")
public class AddressController {

    private final AddressServiceI addressService;

    private final AuthUtil authUtil;


    @PostMapping("")
    public ResponseEntity<ApiResponse> addAddress(@Valid @RequestBody AddressDto addressDto){

        User user = authUtil.loggedInUser();

        AddressDto savedAddress = addressService.addAddress(addressDto, user);
        return new ResponseEntity<>(
                new ApiResponse(true, savedAddress),
                HttpStatus.CREATED
        );
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse> getAddresses(){

       AddressResponse allAddresses = addressService.getAddresses();
        return new ResponseEntity<>(
                new ApiResponse(true, allAddresses),
                HttpStatus.OK
        );
    }

    @GetMapping("{addressId}")
    public ResponseEntity<ApiResponse> getAddresses(@PathVariable Long addressId){

        AddressDto addressDto = addressService.getSIngleAddressById(addressId);
        return new ResponseEntity<>(
                new ApiResponse(true, addressDto),
                HttpStatus.OK
        );
    }

    @GetMapping("user")
    public ResponseEntity<ApiResponse> getAddressesByUser(){

        User user = authUtil.loggedInUser();

        AddressResponse addressResponse = addressService.getAddressesByUser(user);
        return new ResponseEntity<>(
                new ApiResponse(true, addressResponse),
                HttpStatus.OK
        );
    }

    @PutMapping("{addressId}")
    public ResponseEntity<ApiResponse> updateAddress(@PathVariable Long addressId, @Valid @RequestBody AddressDto addressDto){

        AddressDto updatedAddressDto = addressService.updateAddress(addressId, addressDto);
        return new ResponseEntity<>(
                new ApiResponse(true, updatedAddressDto),
                HttpStatus.OK
        );
    }

    @DeleteMapping("{addressId}")
    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable Long addressId){

        String res = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(
                new ApiResponse(true, res),
                HttpStatus.OK
        );
    }
}
