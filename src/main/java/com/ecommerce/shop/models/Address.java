package com.ecommerce.shop.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 3, message = "Street name must be at least 3 characters long")
    private String street;

    private String building;

    @NotBlank
    @Size(min = 3, message = "City name must be at least 3 characters long")
    private String city;


    @NotBlank
    @Size(min = 2, message = "Country name must be at least 2 characters long")
    private String country;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

    public Address(String street, String building, String city, String country) {
        this.street = street;
        this.building = building;
        this.city = city;
        this.country = country;
    }


}
