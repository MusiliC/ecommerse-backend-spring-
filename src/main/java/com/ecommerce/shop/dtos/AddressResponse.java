package com.ecommerce.shop.dtos;

import java.util.List;

import lombok.Data;


@Data
public class AddressResponse {
    private List<AddressDto> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;
}
