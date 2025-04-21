package com.ecommerce.shop.service;

import com.ecommerce.shop.security.dtos.LoginRequest;
import com.ecommerce.shop.security.dtos.LoginResponse;
import com.ecommerce.shop.security.dtos.SignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;

public interface AuthServiceI {

    String registerUser(SignUpRequest signUpRequest);

    LoginResponse authenticateUser(LoginRequest loginRequest);

    ResponseEntity<?> authenticateUserWithCookie(LoginRequest loginRequest);
}
