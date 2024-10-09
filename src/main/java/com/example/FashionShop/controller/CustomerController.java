package com.example.FashionShop.controller;

import com.example.FashionShop.dto.CustomerDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Customer;
import com.example.FashionShop.service.interfaces.ICustomer;
import com.example.FashionShop.utils.Util;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final ICustomer customerService;

    @GetMapping("")
    public ResponseEntity<ResponseDTO<List<CustomerDTO>>> getAllCustomers() {
        return customerService.getAll();
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ResponseDTO<CustomerDTO>> getCustomerById(@PathVariable("customerId") String customerId) {
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        return customerService.findById(customer);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<CustomerDTO>> searchCustomerByEmail(@RequestParam("email") String email) {
        return customerService.findCustomerByEmail(email);
    }

    @PostMapping("")
    public ResponseEntity<ResponseDTO<Customer>> createCustomer(@Valid @RequestBody Customer customer) {
        Util.trimFields(customer);
        return customerService.create(customer);
    }

    @PutMapping("")
    public ResponseEntity<ResponseDTO<Customer>> updateCustomer(@RequestBody Customer customer) {
        Util.trimFields(customer);
        return customerService.update(customer);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<ResponseDTO<String>> deleteCustomer(@PathVariable("customerId") String customerId) {
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        return customerService.delete(customer);
    }

}
