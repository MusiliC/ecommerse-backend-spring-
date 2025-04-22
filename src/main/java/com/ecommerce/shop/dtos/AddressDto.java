package com.ecommerce.shop.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private Long addressId;


    private String street;

    private String building;

    private String city;

    private String country;

}
