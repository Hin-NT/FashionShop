package com.example.FashionShop.service.implement;

import com.example.FashionShop.dto.OrderDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.enums.OrderStatus;
import com.example.FashionShop.model.Order;
import com.example.FashionShop.model.OrderDetail;
import com.example.FashionShop.model.ProductColorSize;
import com.example.FashionShop.repository.OrderDetailRepository;
import com.example.FashionShop.repository.OrderRepository;
import com.example.FashionShop.repository.ProductColorSizeRepository;
import com.example.FashionShop.service.interfaces.IOrder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrder {

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;

    private final ProductColorSizeRepository productColorSizeRepository;

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

        LocalDateTime currentTime = LocalDateTime.now();
        order.setCreatedAt(currentTime);
        order.setUpdatedAt(currentTime);

        if (order.getOrderDetails() == null || order.getOrderDetails().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Order details is empty"));
        }

        try {
            order.setOrderStatus(OrderStatus.PENDING);
            Order orderSaved = orderRepository.save(order);
            order.setOrderId(orderSaved.getOrderId());
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                ProductColorSize productColorSize = productColorSizeRepository
                        .findById(orderDetail.getProductColorSize().getProductColorSizeId())
                        .orElseThrow(() -> new RuntimeException("ProductColorSize not found"));

                int newQuantity = productColorSize.getQuantity() - orderDetail.getQuantity();
                if (newQuantity < 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseDTO<>(null,
                                    "Not enough quantity for product: " + productColorSize.getProductColorSizeId()));
                }
                productColorSize.setQuantity(newQuantity);

                productColorSizeRepository.save(productColorSize);
            }
            logger.info("Order created successfully: {}", orderSaved);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO<>(order, "Order created successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while creating order: {} ", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while creating order" + e.getMessage()));
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
            ProductColorSize productColorSize = productColorSizeRepository.findById(orderDetail.getProductColorSize().getProductColorSizeId())
                    .orElseThrow(() -> new RuntimeException("ProductColorSize not found"));

            int restoredQuantity = productColorSize.getQuantity() + orderDetail.getQuantity();
            productColorSize.setQuantity(restoredQuantity);

            productColorSizeRepository.save(productColorSize);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("Order cancelled successfully");
    }

    @Override
    public ResponseEntity<String> confirmOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getOrderStatus().equals(OrderStatus.PENDING)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order has been cancelled");
        }

        order.setOrderStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        return ResponseEntity.status(HttpStatus.CREATED).body("Order confirmed successfully");
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
        String orderId = order.getOrderId();

        if (!orderRepository.existsById(order.getOrderId())) {
            logger.error("Order with ID {} not found", orderId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO<>(null, "Order with ID: " + orderId + " not found"));
        }

        try {
            order.setUpdatedAt(LocalDateTime.now());

            Order orderUpdated = orderRepository.save(order);
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                orderDetail.setOrder(new Order(order.getOrderId()));
                orderDetailRepository.save(orderDetail);
            }
            logger.info("Order updated successfully with ID: {}", orderUpdated);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(orderUpdated, "Order updated successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while updating order: {} ", order.getOrderId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while updating order" + e.getMessage()));
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

}
