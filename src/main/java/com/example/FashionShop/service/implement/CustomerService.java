package com.example.FashionShop.service.implement;

import com.example.FashionShop.dto.CustomerDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Customer;
import com.example.FashionShop.repository.CustomerRepository;
import com.example.FashionShop.service.interfaces.ICustomer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomer {

    private final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<ResponseDTO<List<CustomerDTO>>> getAll() {
        try {
            List<Customer> customers = customerRepository.findAll();

            if (customers.isEmpty()) {
                logger.info("No Customers found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(new ArrayList<>(), "No Customers found"));
            }
            List<CustomerDTO> customerDTOs = customers.stream()
                    .map(customer -> new CustomerDTO(customer, 0))
                    .toList();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(customerDTOs, "Customers retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while fetching customers: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while fetching customers" + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Customer>> create(Customer customer) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        customer.setCreatedAt(currentDateTime);
        customer.setUpdatedAt(currentDateTime);

        if (customerRepository.existsByEmail(customer.getEmail())) {
            logger.warn("Customer with email: {} already exists", customer.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO<>(null,
                            "Customer with email: " + customer.getEmail() + " already exists"));
        }

        if (!isPasswordValid(customer.getPassword())) {
            logger.warn("Invalid password format for email: {}", customer.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null,
                            "Password must be at least 8 characters long, contain at least one digit, one letter, and one special character"));
        }

        String hashedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(hashedPassword);

        try {
            Customer customerSaved = customerRepository.save(customer);
            logger.info("Customer created successfully with ID: {}", customerSaved.getCustomerId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO<>(customerSaved, "Customer created successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while creating customer: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null, "Error occurred while creating customer"));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<CustomerDTO>> findById(Customer customer) {
        if (customer == null || customer.getCustomerId() == null) {
            logger.warn("Customer or Customer id is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Customer or Customer id is null"));
        }

        logger.info("Searching for customer with ID: {}", customer.getCustomerId());

        try {
            Optional<Customer> customerOptional = customerRepository.findById(customer.getCustomerId());

            if (customerOptional.isPresent()) {
                logger.info("Customer found with ID: {}", customerOptional.get().getCustomerId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(new CustomerDTO(customerOptional.get(), 1),
                                "Customer found with ID: " + customerOptional.get().getCustomerId()));
            } else {
                logger.info("Customer not found with ID: {}", customer.getCustomerId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null, "Customer not found with ID: " + customer.getCustomerId()));
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching customer: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null, "Error occurred while fetching customer"));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Customer>> update(Customer customer) {
        String customerId = customer.getCustomerId();

        if (!customerRepository.existsById(customerId)) {
            logger.warn("Customer with id {} does not exist", customerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO<>(null, "Customer not found"));
        }

        try {
            customer.setUpdatedAt(LocalDateTime.now());

            if (customer.getPassword() != null && !customer.getPassword().isEmpty()) {
                if (!isPasswordValid(customer.getPassword())) {
                    logger.warn("Invalid password format for customer with ID: {}", customerId);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseDTO<>(null,
                                    "Password must be at least 8 characters long, contain at least one digit, one letter, and one special character"));
                }
                String hashedPassword = passwordEncoder.encode(customer.getPassword());
                customer.setPassword(hashedPassword);
            } else {
                Customer existingCustomer = customerRepository.findById(customerId).orElse(null);
                if (existingCustomer != null) {
                    customer.setPassword(existingCustomer.getPassword());
                }
            }

            Customer customerUpdated = customerRepository.save(customer);
            logger.info("Customer updated successfully: {}", customerUpdated);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>
                            (customerUpdated, "Customer updated successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while updating customer: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>
                            (null, "Error occurred while updating customer"));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<String>> delete(Customer customer) {
        if (customer == null || customer.getCustomerId() == null) {
            logger.warn("Customer or CustomerId is null when deleting customer");
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(null, "Customer or CustomerId is null"));
        }

        try {
            String customerId = customer.getCustomerId();
            if (customerRepository.existsById(customerId)) {
                customerRepository.deleteById(customerId);
                logger.info("Customer deleted successfully with ID: {}", customerId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body((new ResponseDTO<>(null, "Customer deleted successfully")));
            } else {
                logger.warn("Customer not found with ID: {}", customerId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null, "Customer not found with ID: " + customerId));
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting customer: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while deleting customer: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<CustomerDTO>> findCustomerByEmail(String email) {
        try {
            Optional<Customer> customerOptional = customerRepository.findCustomerByEmail(email);

            if (customerOptional.isPresent()) {
                logger.info("Customer found with email: {}", customerOptional.get().getCustomerId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(new CustomerDTO(customerOptional.get(), 1),
                                "Customer found with email: " + customerOptional.get().getCustomerId()));
            } else {
                logger.info("Customer not found with email: {}", email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null, "Customer not found with email: " + email));
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching customer: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while searching customer: " + e.getMessage()));
        }
    }

    private boolean isPasswordValid(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasLetter = password.chars().anyMatch(Character::isLetter);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> "!@#$%^&*()_+[]{}|;:',.<>/?".indexOf(ch) >= 0);

        return hasLetter && hasDigit && hasSpecial;
    }

}