package com.ecommerce.shop.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @ToString.Exclude
    @Column(length = 20, name = "role_name")
    @Enumerated(EnumType.STRING)
    private AppRole roleName;

    public Role(AppRole roleName) {
        this.roleName = roleName;
    }

    public Role() {
    }
}
