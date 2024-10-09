package com.example.FashionShop.service.implement;

import com.example.FashionShop.dto.ProductColorSizeDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Filter;
import com.example.FashionShop.model.ProductColorSize;
import com.example.FashionShop.repository.ProductColorSizeRepository;
import com.example.FashionShop.service.interfaces.IProductColorSize;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductColorSizeService implements IProductColorSize {

    private final Logger logger = LoggerFactory.getLogger(ProductColorSizeService.class);
    private final ProductColorSizeRepository productColorSizeRepository;
    private final ReviewService reviewService;

    @Override
    public ResponseEntity<ResponseDTO<List<ProductColorSizeDTO>>> getAll() {
        try {
            List<ProductColorSize> productColorSizes = productColorSizeRepository.findAll();
            List<ProductColorSizeDTO> productColorSizeDTOs = productColorSizes.stream()
                    .map(productColorSize -> new ProductColorSizeDTO(productColorSize, 0))
                    .toList();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(productColorSizeDTOs, "ProductColorSizes retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error retrieving product color sizes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null, "Error retrieving product color sizes: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<ProductColorSize>> create(ProductColorSize productColorSize) {

        LocalDateTime currentDateTime = LocalDateTime.now();
        productColorSize.setCreatedAt(currentDateTime);
        productColorSize.setUpdatedAt(currentDateTime);

        try {
            if (productColorSizeRepository.existsByProductColorAndSize(productColorSize.getProductColor(), productColorSize.getSize())) {
                logger.error("ProductColorSize already exists with color {} and size {}",
                        productColorSize.getProductColor(), productColorSize.getSize());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ResponseDTO<>(null,
                                "ProductColorSize already exists with color " + productColorSize.getProductColor()));
            }
            ProductColorSize productColorSizeSaved = productColorSizeRepository.save(productColorSize);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO<>(productColorSizeSaved, "Product color size created successfully"));
        } catch (Exception e) {
            logger.error("Error creating ProductColorSize: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null, "Error creating ProductColorSize: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<ProductColorSizeDTO>> findById(ProductColorSize productColorSize) {
        try {
            return productColorSizeRepository.findById(productColorSize.getProductColorSizeId())
                    .map(found -> ResponseEntity
                            .ok(new ResponseDTO<>(new ProductColorSizeDTO(found, 1), "ProductColorSize found")))
                    .orElseGet(() -> {
                        logger.error("ProductColorSize not found with ID: {}", productColorSize.getProductColorSizeId());
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ResponseDTO<>(null,
                                        "ProductColorSize not found with ID: " + productColorSize.getProductColorSizeId()));
                    });
        } catch (Exception e) {
            logger.error("Error finding ProductColorSize: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null, "Error finding ProductColorSize: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<ProductColorSize>> update(ProductColorSize productColorSize) {
        try {
            productColorSize.setUpdatedAt(LocalDateTime.now());
            return productColorSizeRepository.findById(productColorSize.getProductColorSizeId())
                    .map(existing -> {
                        ProductColorSize productColorSizeUpdated = productColorSizeRepository.save(existing);
                        return ResponseEntity.status(HttpStatus.OK)
                                .body(new ResponseDTO<>(productColorSizeUpdated,
                                        "Product color size updated successfully"));
                    })
                    .orElseGet(() -> {
                        logger.error("ProductColorSize not found: {}", productColorSize.getProductColorSizeId());
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ResponseDTO<>(null,
                                        "ProductColorSize not found: " + productColorSize.getProductColorSizeId()));
                    });
        } catch (Exception e) {
            logger.error("Error updating ProductColorSize: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null, "Error updating ProductColorSize: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<String>> delete(ProductColorSize productColorSize) {
        if (productColorSize == null || productColorSize.getProductColorSizeId() == null) {
            logger.warn("ProductColorSize or ProductColorSize ID is null when deleting");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "ProductColorSize or ProductColorSize ID is null"));
        }

        try {
            if (!productColorSizeRepository.existsById(productColorSize.getProductColorSizeId())) {
                logger.warn("ProductColorSize not found: {}", productColorSize.getProductColorSizeId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null, "ProductColorSize not found"));
            }

            productColorSizeRepository.deleteById(productColorSize.getProductColorSizeId());
            logger.info("ProductColorSize with ID: {} deleted successfully", productColorSize.getProductColorSizeId());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(null, "ProductColorSize deleted successfully"));
        } catch (Exception e) {
            logger.error("Error deleting ProductColorSize with ID: {}: {}", productColorSize.getProductColorSizeId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error deleting ProductColorSize: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<List<ProductColorSizeDTO>>> filterProduct(Filter filter) {
        List<ProductColorSize> products = productColorSizeRepository.filterProducts(
                filter.getCategoriesId(),
                filter.getStylesId(),
                filter.getColorsId(),
                filter.getSizeId(),
                filter.getStartPrice(),
                filter.getEndPrice()
        );

        List<ProductColorSizeDTO> productColorSizeDTOList = products.stream().map(productColorSize -> new ProductColorSizeDTO(productColorSize, 0)).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO<>(productColorSizeDTOList, "Product color sizes retrieved successfully"));
    }
}
