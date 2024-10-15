package com.example.FashionShop.service.implement;

import com.example.FashionShop.dto.OrderDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.enums.OrderStatus;
import com.example.FashionShop.enums.PeriodType;
import com.example.FashionShop.model.*;
import com.example.FashionShop.repository.*;
import com.example.FashionShop.service.interfaces.IOrder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrder {

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;

    private final ProductColorSizeRepository productColorSizeRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Override
    public ResponseEntity<ResponseDTO<List<OrderDTO>>> getAll() {
        try {
            List<Order> orders = orderRepository.findAll();
            if (orders.isEmpty()) {
                logger.info("No Orders found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ResponseDTO<>(null, "No Orders found"));
            }
            List<OrderDTO> orderDTOs = orders.stream()
                    .map(order -> new OrderDTO(order, 0))
                    .toList();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(orderDTOs, "Orders retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while fetching orders: {} ", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while fetching orders" + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Order>> create(Order order) {

        if (order.getOrderDetails() == null || order.getOrderDetails().isEmpty()) {
            logger.error("OrderDetails is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "OrderDetails is null or empty"));
        }

        if (order.getCustomer() == null || order.getCustomer().getCustomerId() == null
                || order.getCustomer().getCustomerId().isBlank()) {
            logger.error("Customer or CustomerId is null or blank");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, " Customer or CustomerId is null or blank"));
        }

        try {
            if (!customerRepository.existsById(order.getCustomer().getCustomerId())) {
                logger.error("Customer does not exist");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null, "Customer does not exist"));
            }

            LocalDateTime currentDateTime = LocalDateTime.now();
            order.setCreatedAt(currentDateTime);
            order.setUpdatedAt(currentDateTime);

            order.setOrderStatus(OrderStatus.PENDING);

            orderRepository.save(order);

            for (OrderDetail orderDetail : order.getOrderDetails()) {
                if (orderDetail.getProductColorSize() == null || orderDetail.getProductColorSize().getProductColorSizeId() == null
                        || orderDetail.getProductColorSize().getProductColorSizeId().isBlank()) {
                    logger.error("ProductColorSize or ProductColorSizeID is null or blank");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseDTO<>(null, "ProductColorSize or ProductColorSizeID is null"));
                }

                Optional<ProductColorSize> productColorSizeOptional = productColorSizeRepository
                        .findById(orderDetail.getProductColorSize().getProductColorSizeId());
                if (productColorSizeOptional.isEmpty()) {
                    logger.error("ProductColorSizeID not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO<>(null, "ProductColorSizeID not found"));
                }

                ProductColorSize product = productColorSizeOptional.get();

                int newQuantity = product.getQuantity() - orderDetail.getProductColorSize().getQuantity();
                if (newQuantity < 0) {
                    logger.error("Quantity is negative");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseDTO<>(null, "Quantity is negative"));
                }
                product.setQuantity(newQuantity);
                orderDetail.setOrder(new Order(order.getOrderId()));

                orderDetailRepository.save(orderDetail);
            }
            logger.info("Order created successfully");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO<>(order, "Order created successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while creating order: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while creating order: {}" + e.getMessage()));
        }
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void checkPendingOrders() {
        List<Order> orders = orderRepository.findAllByOrderStatus(OrderStatus.PENDING);
        for (Order order : orders) {
            if (order.getCreatedAt().plusMinutes(1).isBefore(LocalDateTime.now())) {
                cancelOrder(order);
            }
        }
    }

    @Override
    public ResponseEntity<String> cancelOrder(Order order) {

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        for (OrderDetail orderDetail : order.getOrderDetails()) {
            Optional<ProductColorSize> productColorSize = productColorSizeRepository
                    .findById(orderDetail.getProductColorSize().getProductColorSizeId());
            if (productColorSize.isEmpty()) {
                logger.error("ProductColorSizeID not found");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ProductColorSizeID not found");
            }
            ProductColorSize product = productColorSize.get();

            int restoreQuantity = product.getQuantity() + orderDetail.getProductColorSize().getQuantity();
            product.setQuantity(restoreQuantity);

            productColorSizeRepository.save(product);
        }
        logger.info("Order cancelled successfully");
        return ResponseEntity.status(HttpStatus.OK).body("Order cancelled successfully");
    }

    @Override
    public ResponseEntity<String> confirmOrder(String orderId) {

        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            logger.error("Order not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }
        Order order = orderOptional.get();

        if (!order.getOrderStatus().equals(OrderStatus.PENDING)) {
            logger.error("Order status is not PENDING");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Order status is not PENDING");
        }
        order.setOrderStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);
        logger.info("Order confirmed successfully");
        return ResponseEntity.status(HttpStatus.OK).body("Order confirmed successfully");
    }

    @Override
    public ResponseEntity<ResponseDTO<OrderDTO>> findById(Order order) {
        if (order == null || order.getOrderId() == null) {
            logger.warn("Order or OrderId is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Order or OrderId is null"));
        }

        try {
            Optional<Order> orderOptional = orderRepository.findById(order.getOrderId());

            if (orderOptional.isPresent()) {
                logger.info("Order found with ID: {}", order.getOrderId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(new OrderDTO(orderOptional.get(), 1),
                                "Order found with ID: " + order.getOrderId()));
            } else {
                logger.warn("Order with IDL {} not found", order.getOrderId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null,
                                "Order with ID: " + order.getOrderId() + " not found"));
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching order: {} ", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while fetching order" + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Order>> update(Order order) {
        if (order.getOrderDetails() == null || order.getOrderDetails().isEmpty()) {
            logger.error("OrderDetails is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "OrderDetails is null or empty"));
        }

        if (order.getCustomer() == null || order.getCustomer().getCustomerId() == null
                || order.getCustomer().getCustomerId().isBlank()) {
            logger.error("Customer or CustomerId is null or blank");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Customer or CustomerId is null or blank"));
        }

        try {
            String orderId = order.getOrderId();

            if (!orderRepository.existsById(orderId)) {
                logger.error("Order with ID {} not found", orderId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null, "Order with ID: " + orderId + " not found"));
            }

            for (OrderDetail orderDetail : order.getOrderDetails()) {
                if (orderDetail.getProductColorSize() == null ||
                        orderDetail.getProductColorSize().getProductColorSizeId() == null ||
                        orderDetail.getProductColorSize().getProductColorSizeId().isBlank()) {

                    logger.error("ProductColorSize is null or blank");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseDTO<>(null, "ProductColorSize is null or blank"));
                }

                Optional<ProductColorSize> productColorSizeOptional =
                        productColorSizeRepository.findById(orderDetail.getProductColorSize().getProductColorSizeId());

                if (productColorSizeOptional.isEmpty()) {
                    logger.error("ProductColorSize not found");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseDTO<>(null, "ProductColorSize not found"));
                }

                ProductColorSize productColorSize = productColorSizeOptional.get();

                List<OrderDetail> existingOrderDetail =
                        orderDetailRepository.findByOrder_OrderIdAndProductColorSize_ProductColorSizeId(orderId,
                                orderDetail.getProductColorSize().getProductColorSizeId());

                if (!existingOrderDetail.isEmpty()) {
                    int oldQuantity = existingOrderDetail.get(0).getQuantity();
                    int newQuantity = orderDetail.getQuantity();
                    int difference = newQuantity - oldQuantity;

                    if (difference > 0) {
                        if (productColorSize.getQuantity() < difference) {
                            logger.error("Not enough quantity in stock");
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body(new ResponseDTO<>(null, "Not enough quantity in stock"));
                        }
                        productColorSize.setQuantity(productColorSize.getQuantity() - difference);
                    } else if (difference < 0) {
                        productColorSize.setQuantity(productColorSize.getQuantity() + Math.abs(difference));
                    }
                } else {
                    if (productColorSize.getQuantity() < orderDetail.getQuantity()) {
                        logger.error("Not enough quantity in stock");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ResponseDTO<>(null, "Not enough quantity in stock"));
                    }
                    productColorSize.setQuantity(productColorSize.getQuantity() - orderDetail.getQuantity());
                }

                productColorSizeRepository.save(productColorSize);
                orderDetail.setOrder(new Order(order.getOrderId()));
                orderDetailRepository.save(orderDetail);
            }

            logger.info("Order updated successfully with ID: {}", orderId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(order, "Order updated successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while updating order: {} ", order.getOrderId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null, "Error occurred while updating order" + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<String>> delete(Order order) {
        if (order == null || order.getOrderId() == null) {
            logger.warn("Order or OrderId is null when deleting order");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Order or OrderId is null"));
        }

        try {
            if (orderRepository.existsById(order.getOrderId())) {
                orderRepository.deleteById(order.getOrderId());
                logger.info("Order with ID: {} deleted successfully", order.getOrderId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(null, "Order deleted successfully"));
            } else {
                logger.warn("Order with ID: {} not found", order.getOrderId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null,
                                String.format("Order not found with ID: %s", order.getOrderId())));
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting order with ID: {}: {}", order.getOrderId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while deleting order: " + e.getMessage()));
        }
    }

    @Override
    public Order getOrder(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public ResponseEntity<Double> getTotalRevenueByPeriodTime(PeriodType periodType, long quantity) {
        try {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = switch (periodType) {
                case WEEK -> endDate.minusWeeks(quantity);
                case MONTH -> endDate.minusMonths(quantity);
                case QUARTER -> endDate.minusMonths(quantity * 3);
                case YEAR -> endDate.minusYears(quantity);
                default -> throw new IllegalArgumentException("Invalid period type");
            };
            List<Order> orders = orderRepository
                    .findByOrderStatusAndDateBetween(OrderStatus.DELIVERED, startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));

            double total = orders.stream()
                    .mapToDouble(Order::getTotalPrice)
                    .sum();
            return ResponseEntity.status(HttpStatus.OK).body(total);
        } catch (Exception e) {
            logger.error("Error occurred while fetching total revenue: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0.0);
        }
    }

    private boolean isStatusAChangeAllowed(OrderStatus orderStatus) {
        return orderStatus == OrderStatus.SHIPPED ||
                orderStatus == OrderStatus.DELIVERED ||
                orderStatus == OrderStatus.RETURNED;
    }

    @Override
    public ResponseEntity<String> updateOrderStatus(String orderId, OrderStatus orderStatus) {
        if (!isStatusAChangeAllowed(orderStatus)) {
            logger.error("Order with ID {} is not a change allowed status", orderId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order with ID: " + orderId + " is not a change allowed status");
        }
        try {
            Order order = orderRepository.findById(orderId).orElse(null);

            if (order == null) {
                logger.error("Order with ID {} not found", orderId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order with ID: " + orderId + " not found");
            }
            order.setUpdatedAt(LocalDateTime.now());
            order.setOrderStatus(orderStatus);
            orderRepository.save(order);
            return ResponseEntity.status(HttpStatus.OK).body("Order updated successfully");
        } catch (Exception e) {
            logger.error("Error occurred while updating order: {}", orderId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while updating order: " + e.getMessage());
        }
    }
}
