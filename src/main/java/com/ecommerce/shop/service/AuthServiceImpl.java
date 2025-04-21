package com.ecommerce.shop.service;


import com.ecommerce.shop.exceptions.APIException;
import com.ecommerce.shop.models.AppRole;
import com.ecommerce.shop.models.Role;
import com.ecommerce.shop.models.User;
import com.ecommerce.shop.repository.RoleRepository;
import com.ecommerce.shop.repository.UserRepository;
import com.ecommerce.shop.security.dtos.LoginRequest;
import com.ecommerce.shop.security.dtos.LoginResponse;
import com.ecommerce.shop.security.dtos.SignUpRequest;
import com.ecommerce.shop.security.jwt.JwtUtils;
import com.ecommerce.shop.security.services.UserDetailsImpl;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthServiceI {


    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;


    @Override
    public String registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            throw new APIException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new APIException("Error: Email is already in use!");
        }

        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword())
        );

        Set<Role> roles = processRoles(signUpRequest.getRoles());
        user.setRoles(roles);

        userRepository.save(user);
        return "User registered successfully!";
    }

    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        return new LoginResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                roles,
                jwt
        );


    }

    @Override
    public ResponseEntity<?> authenticateUserWithCookie(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();


        ResponseCookie jwt = jwtUtils.generateJwtCookie(userDetails);
        System.out.println("JWT Cookie: " + jwt.toString());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        LoginResponse res = new LoginResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                roles,
                jwt.toString()
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwt.toString())
                .body(res);
    }

    @Override
    public ResponseEntity<LoginResponse> getCurrentUser(Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        LoginResponse res = new LoginResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                roles
        );

        return ResponseEntity.ok()
                .body(res);
    }


    private Set<Role> processRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new APIException("Error: Role is not found."));
            roles.add(userRole);
            return roles;
        }

        strRoles.forEach(role -> {
            System.out.println("::::::::Role:::::::" + role);
            switch (role.toLowerCase()) {

                case "admin" -> {
                    Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                            .orElseThrow(() -> new APIException("Error: Role is not found."));
                    roles.add(adminRole);
                }
                case "seller" -> {
                    Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                            .orElseThrow(() -> new APIException("Error: Role is not found."));
                    roles.add(sellerRole);
                }
                default -> {
                    Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                            .orElseThrow(() -> new APIException("Error: Role is not found."));
                    roles.add(userRole);
                }
            }
        });

        return roles;
    }
}
