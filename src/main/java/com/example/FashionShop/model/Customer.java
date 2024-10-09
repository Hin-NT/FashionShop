package com.example.FashionShop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "customer_id")
    String customerId;

    @NotBlank
    @Column(name = "customer_name")
    String customerName;

    @NotBlank
    @Size(max = 15)
    @Column(name = "phone_number")
    String phoneNumber;

    @NotBlank
    @Email
    @Column(name = "email", unique = true)
    String email;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one digit, one letter, and one special character")
    @Column(name = "password")
    String password;

    @Column(name = "address")
    String address;

    @OneToMany(mappedBy = "customer")
    List<Review> reviews;

    @OneToMany(mappedBy = "customer")
    List<Order> orders;

    public Customer(String customerId, String customerName, String phoneNumber, String email, String password, String address) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.address = address;
    }
}
