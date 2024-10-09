package com.example.FashionShop.repository;


import com.example.FashionShop.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findCustomerByEmail(String email);

    boolean existsByEmail(String email);
}
