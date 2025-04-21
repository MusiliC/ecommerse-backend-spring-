package com.ecommerce.shop.controllers;

import com.ecommerce.shop.repository.RoleRepository;
import com.ecommerce.shop.repository.UserRepository;
import com.ecommerce.shop.response.ApiResponse;
import com.ecommerce.shop.security.dtos.LoginRequest;
import com.ecommerce.shop.security.dtos.LoginResponse;
import com.ecommerce.shop.security.dtos.SignUpRequest;
import com.ecommerce.shop.security.jwt.JwtUtils;
import com.ecommerce.shop.service.AuthServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    AuthServiceI authServiceI;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        String res = authServiceI.registerUser(signUpRequest);
        return new ResponseEntity<>(
                new ApiResponse(true, res),
                HttpStatus.CREATED
        );

    }

    @PostMapping("signin")
    public ResponseEntity<ApiResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        LoginResponse res = authServiceI.authenticateUser(loginRequest);
        return new ResponseEntity<>(
                new ApiResponse(true, res),
                HttpStatus.CREATED
        );

    }

    @PostMapping("login")
    public ResponseEntity<?> authenticateUserWithCookie(@Valid @RequestBody LoginRequest loginRequest) {
        return authServiceI.authenticateUserWithCookie(loginRequest);
    }
}
