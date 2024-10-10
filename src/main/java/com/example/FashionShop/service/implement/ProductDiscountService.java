package com.example.FashionShop.service.implement;

import com.example.FashionShop.dto.ProductColorSizeDTO;
import com.example.FashionShop.dto.ProductDiscountDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.ProductDiscount;
import com.example.FashionShop.repository.ProductDiscountRepository;
import com.example.FashionShop.service.interfaces.IProductDiscount;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductDiscountService implements IProductDiscount {

    private final Logger logger = LoggerFactory.getLogger(ProductDiscountService.class);

    private final ProductDiscountRepository productDiscountRepository;

    @Override
    public ResponseEntity<ResponseDTO<List<ProductDiscountDTO>>> getAll() {
        try {
            List<ProductDiscount> productDiscounts = (List<ProductDiscount>) productDiscountRepository.findAll();

            if (productDiscounts.isEmpty()) {
                logger.info("No ProductDiscounts found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ResponseDTO<>(null, "No ProductDiscounts found"));
            }

            List<ProductDiscountDTO> productDiscountDTOs = productDiscounts.stream()
                    .map(ProductDiscountDTO::new)
                    .toList();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(productDiscountDTOs, "ProductDiscounts retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while fetching ProductDiscounts: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while fetching ProductDiscounts: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<ProductDiscount>> create(ProductDiscount productDiscount) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        productDiscount.setCreatedAt(currentDateTime);
        productDiscount.setUpdatedAt(currentDateTime);

        try {
            ProductDiscount productDiscountSaved = productDiscountRepository.save(productDiscount);
            logger.info("ProductDiscount created successfully with ID: {}", productDiscountSaved.getProductDiscountId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO<>(productDiscountSaved, "ProductDiscount created successfully"));

        } catch (Exception e) {
            logger.error("Error occurred while creating ProductDiscount: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while creating ProductDiscount: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<ProductDiscountDTO>> findById(ProductDiscount productDiscount) {
        if (productDiscount == null || productDiscount.getProductDiscountId() == null) {
            logger.info("ProductDiscount or productDiscount ID is null in findById");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "ProductDiscount or productDiscount ID is null"));
        }

        try {
            Optional<ProductDiscount> productDiscountOptional = productDiscountRepository.findById(productDiscount.getProductDiscountId());
            if (productDiscountOptional.isPresent()) {
                ProductDiscount discount = productDiscountOptional.get();
                ProductDiscountDTO productDiscountDTO = new ProductDiscountDTO(
                        discount.getProductDiscountId(),
                        new ProductColorSizeDTO(discount.getProductColorSize(), 0),
                        discount.getDiscount(),
                        discount.getPercent()
                );
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(productDiscountDTO, "ProductDiscount found"));
            } else {
                logger.warn("ProductDiscount with ID: {} not found in findById", productDiscount.getProductDiscountId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null,
                                "ProductDiscount with ID: " + productDiscount.getProductDiscountId() + " not found"));
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching ProductDiscount: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while fetching ProductDiscount: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<ProductDiscount>> update(ProductDiscount productDiscount) {
        String productDiscountId = productDiscount.getProductDiscountId();

        if (!productDiscountRepository.existsById(productDiscountId)) {
            logger.warn("ProductDiscount with ID: {} does not exist", productDiscountId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO<>(null,
                            "ProductDiscount with ID: " + productDiscountId + " not found"));
        }

        try {
            productDiscount.setUpdatedAt(LocalDateTime.now());

            ProductDiscount productDiscountUpdated = productDiscountRepository.save(productDiscount);
            logger.info("ProductDiscount updated successfully: {}", productDiscountUpdated);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(productDiscountUpdated, "ProductDiscount updated successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while updating ProductDiscount: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while updating ProductDiscount: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<String>> delete(ProductDiscount productDiscount) {
        if (productDiscount == null || productDiscount.getProductDiscountId() == null || productDiscount.getProductDiscountId().isEmpty()) {
            logger.info("ProductDiscount or ProductDiscountId is null or empty when deleting ProductDiscount");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null,
                            "ProductDiscount or ProductDiscountId is null or empty"));
        }

        try {
            if (productDiscountRepository.existsById(productDiscount.getProductDiscountId())) {
                productDiscountRepository.deleteById(productDiscount.getProductDiscountId());
                logger.info("ProductDiscount with ID: {} deleted successfully", productDiscount.getProductDiscountId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(null, "ProductDiscount deleted successfully"));
            } else {
                logger.warn("ProductDiscount with ID: {} not found", productDiscount.getProductDiscountId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null,
                                "ProductDiscount not found with ID: " + productDiscount.getProductDiscountId()));
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting ProductDiscount with ID: {}: {}", productDiscount.getProductDiscountId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while deleting ProductDiscount: " + e.getMessage()));
        }
    }

}
