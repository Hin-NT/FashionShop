package com.example.FashionShop.dto;

import com.example.FashionShop.model.Customer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDTO {

    String customerId;
    String customerName;
    String customerEmail;
    String phoneNumber;
    String address;
    List<ReviewDTO> reviews;
    List<OrderDTO> orders;

    public CustomerDTO(Customer customer, int choose) {
        if (customer != null) {
            this.customerId = customer.getCustomerId();
            this.customerName = customer.getCustomerName();
            this.customerEmail = customer.getEmail();
            this.phoneNumber = customer.getPhoneNumber();
            this.address = customer.getAddress();

            if (choose == 1) {
                this.reviews = customer.getReviews()
                        .stream()
                        .map(review -> new ReviewDTO(review))
                        .toList();

                this.orders = customer.getOrders()
                        .stream()
                        .map(order -> new OrderDTO(order, 0))
                        .toList();
            }
        } else {
            this.customerId = null;
            this.customerName = null;
            this.customerEmail = null;
            this.phoneNumber = null;
            this.address = null;
            this.reviews = null;
            this.orders = null;
        }
    }
}
