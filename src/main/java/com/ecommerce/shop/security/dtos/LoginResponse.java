package com.ecommerce.shop.security.dtos;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {
    private Long id;
    private String jwtToken;
    private String username;
    private List<String> roles;

    public LoginResponse( Long id, String username, List<String> roles,  String jwtToken) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.jwtToken = jwtToken;

    }
}
