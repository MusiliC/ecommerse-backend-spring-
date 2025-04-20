package com.ecommerce.shop.models;

public enum AppRole {

    ROLE_USER("user"),
    ROLE_ADMIN("admin"),
    ROLE_SELLER("seller");

    public final String role;

    AppRole(String role) {
        this.role = role;
    }

    public String getName() {
        return role;
    }
}
