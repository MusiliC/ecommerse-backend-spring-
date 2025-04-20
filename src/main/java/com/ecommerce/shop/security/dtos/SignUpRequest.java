package com.ecommerce.shop.security.dtos;

import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SignUpRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 4, max = 40)
    private String password;

    @NotBlank
    @Email
    private String email;

    private Set<String> roles;
}
