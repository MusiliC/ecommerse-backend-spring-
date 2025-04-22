package com.ecommerce.shop.models;

public enum OrderStatus {
    PENDING("Pending"),
    PROCESSING("Processing"),
    ACCEPTED("Accepted"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded");

    public final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return status;
    }
}
