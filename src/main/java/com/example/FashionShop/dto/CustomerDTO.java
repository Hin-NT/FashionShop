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
    List<String> reviewIds;
    List<String> orderIds;

    public CustomerDTO(Customer customer, int choose) {
        if (customer != null) {
            this.customerId = customer.getCustomerId();
            this.customerName = customer.getCustomerName();
            this.customerEmail = customer.getEmail();
            this.phoneNumber = customer.getPhoneNumber();
            this.address = customer.getAddress();

            if (choose == 1) {
                this.reviewIds = customer.getReviews()
                        .stream()
                        .map(review -> review.getReviewId())
                        .toList();

                this.orderIds = customer.getOrders()
                        .stream()
                        .map(order -> order.getOrderId())
                        .toList();
            }
        } else {
            this.customerId = null;
            this.customerName = null;
            this.customerEmail = null;
            this.phoneNumber = null;
            this.address = null;
            this.reviewIds = null;
            this.orderIds = null;
        }
    }
}
