package com.example.FashionShop.service.implement;

import com.example.FashionShop.dto.OrderDetailDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.OrderDetail;
import com.example.FashionShop.repository.OrderDetailRepository;
import com.example.FashionShop.repository.OrderRepository;
import com.example.FashionShop.service.interfaces.IOrderDetail;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetail {

    private final Logger logger = LoggerFactory.getLogger(OrderDetailService.class);

    private final OrderDetailRepository orderDetailRepository;

    private final OrderRepository orderRepository;


    @Override
    public ResponseEntity<ResponseDTO<List<OrderDetailDTO>>> getAll() {
        try {
            List<OrderDetail> orderDetails = orderDetailRepository.findAll();
            if (orderDetails.isEmpty()) {
                logger.info("No Order Details found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ResponseDTO<>(null, "No Order Details found"));
            }
            List<OrderDetailDTO> orderDetailDTOs = orderDetails.stream()
                    .map(OrderDetailDTO::new)
                    .toList();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(orderDetailDTOs, "OrderDetails retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while fetching Order Details: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while fetching Order Details" + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<OrderDetail>> create(OrderDetail orderDetail) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO<OrderDetailDTO>> findById(OrderDetail orderDetail) {
        if (orderDetail.getOrderDetailId() == null) {
            logger.warn("Order Detail id is null in update");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "OrderDetailId is null in update"));
        }
        try {
            Optional<OrderDetail> orderDetailOptional = orderDetailRepository.findById(orderDetail.getOrderDetailId());
            if (orderDetailOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(new OrderDetailDTO(orderDetailOptional.get()),
                                "OrderDetail found"));
            } else {
                logger.warn("Order Detail id {} not found", orderDetail.getOrderDetailId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null, "OrderDetail not found"));
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching Order Detail: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null, "Error occurred while fetching Order Detail"));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<OrderDetail>> update(OrderDetail orderDetail) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO<String>> delete(OrderDetail orderDetail) {
        if (orderDetail.getOrderDetailId() == null) {
            logger.warn("Order Detail ID is null in delete");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "OrderDetail ID is null"));
        }

        try {
            if (orderRepository.existsById(orderDetail.getOrderDetailId())) {
                orderRepository.deleteById(orderDetail.getOrderDetailId());
                logger.info("Order with ID {} deleted successfully", orderDetail.getOrderDetailId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(null,
                                "Order with ID " + orderDetail.getOrderDetailId() + " deleted successfully"));
            } else {
                logger.warn("Order with ID {} not found", orderDetail.getOrderDetailId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null,
                                "Order with ID " + orderDetail.getOrderDetailId() + " not found"));
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting order with ID {}: {}", orderDetail.getOrderDetailId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while deleting order: " + e.getMessage()));
        }
    }


}
