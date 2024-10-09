package com.example.FashionShop.service.interfaces;

import com.example.FashionShop.dto.CustomerDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Customer;
import org.springframework.http.ResponseEntity;

public interface ICustomer extends IService<Customer, CustomerDTO> {
    ResponseEntity<ResponseDTO<CustomerDTO>> findCustomerByEmail(String email);
}
