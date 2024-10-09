package com.example.FashionShop.service.implement;

import com.example.FashionShop.dto.ProductColorDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Image;
import com.example.FashionShop.model.ProductColor;
import com.example.FashionShop.repository.ProductColorRepository;
import com.example.FashionShop.service.interfaces.IProductColor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductColorService implements IProductColor {

    private final Logger logger = LoggerFactory.getLogger(ProductColorService.class);
    private final ProductColorRepository productColorRepository;

    @Override
    public ResponseEntity<ResponseDTO<List<ProductColorDTO>>> getAll() {
        try {
            List<ProductColor> productColors = productColorRepository.findAll();
            if (productColors.isEmpty()) {
                logger.info("No ProductColors Found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ResponseDTO<>(new ArrayList<>(), "No ProductColors Found"));
            }

            List<ProductColorDTO> productColorDTOs = productColors.stream()
                    .map(productColor -> new ProductColorDTO(productColor, 1))
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(productColorDTOs, "ProductColors retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while fetching ProductColors: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(new ArrayList<>(),
                            "Error occurred while fetching ProductColors: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<ProductColor>> create(ProductColor productColor) {

        LocalDateTime currentDateTime = LocalDateTime.now();
        productColor.setCreatedAt(currentDateTime);
        productColor.setUpdatedAt(currentDateTime);

        try {
            ResponseEntity<ResponseDTO<ProductColor>> conflictResponse = checkProductColorConflict(productColor);
            if (conflictResponse != null) {
                return conflictResponse;
            }

            ProductColor productColorSaved = productColorRepository.save(productColor);
            productColor.setProductColorId(productColorSaved.getProductColorId());
            logger.info("Product color saved: {}", productColorSaved.getProductColorId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO<>(productColor, "ProductColor saved"));
        } catch (Exception e) {
            logger.error("Error occurred while creating ProductColor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while creating ProductColor: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<ProductColorDTO>> findById(ProductColor productColor) {
        if (productColor == null || productColor.getProductColorId() == null) {
            logger.warn("ProductColor or ProductColor ID is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "ProductColor or ProductColor ID is null"));
        }

        try {
            Optional<ProductColor> optionalProductColor = productColorRepository.findById(productColor.getProductColorId());
            if (optionalProductColor.isPresent()) {
                ProductColorDTO productColorDTO = new ProductColorDTO(optionalProductColor.get(), 1);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(productColorDTO, "ProductColor found"));
            } else {
                logger.warn("ProductColor with ID: {} not found", productColor.getProductColorId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null,
                                "ProductColor with ID: " + productColor.getProductColorId() + " not found"));
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching ProductColor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while fetching ProductColor: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<ProductColor>> update(ProductColor productColor) {
        if (!productColorRepository.existsById(productColor.getProductColorId())) {
            logger.warn("ProductColor with ID: {} does not exist", productColor.getProductColorId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO<>(null,
                            "ProductColor with ID: " + productColor.getProductColorId() + " does not exist"));
        }

        try {
            ResponseEntity<ResponseDTO<ProductColor>> conflictResponse = checkProductColorConflict(productColor);
            if (conflictResponse != null) {
                return conflictResponse;
            }

            ProductColor existingProductColor = productColorRepository.findById(productColor.getProductColorId())
                    .orElseThrow(() -> new RuntimeException("ProductColor not found"));

            existingProductColor.setColor(productColor.getColor());
            existingProductColor.setProduct(productColor.getProduct());

            if (existingProductColor.getImageList() == null) {
                existingProductColor.setImageList(new ArrayList<>());
            } else {
                existingProductColor.getImageList().clear();
            }

            for (Image image : productColor.getImageList()) {
                if (image.getImageUrl() == null) {
                    logger.error("Image URL cannot be null for Image ID: {}", image.getImageId());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseDTO<>(null,
                                    "Image URL cannot be null for Image ID: " + image.getImageId()));
                }
                image.setProductColor(existingProductColor);
                existingProductColor.getImageList().add(image);
            }

            existingProductColor.setUpdatedAt(LocalDateTime.now());
            ProductColor productColorUpdated = productColorRepository.save(existingProductColor);
            logger.info("ProductColor updated successfully: {}", productColorUpdated.getProductColorId());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(productColorUpdated, "ProductColor updated successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while updating ProductColor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while updating ProductColor: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<String>> delete(ProductColor productColor) {
        if (productColor == null || productColor.getProductColorId() == null) {
            logger.warn("ProductColor or ProductColor ID is null when deleting");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "ProductColor or ProductColor ID is null"));
        }

        try {
            if (productColorRepository.existsById(productColor.getProductColorId())) {
                productColorRepository.deleteById(productColor.getProductColorId());
                logger.info("ProductColor with ID: {} deleted successfully", productColor.getProductColorId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(null, "ProductColor deleted successfully"));
            } else {
                logger.warn("ProductColor with ID: {} not found", productColor.getProductColorId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null,
                                String.format("ProductColor not found with ID: %s", productColor.getProductColorId())));
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting ProductColor with ID: {}: {}", productColor.getProductColorId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while deleting ProductColor: " + e.getMessage()));
        }
    }

    private ResponseEntity<ResponseDTO<ProductColor>> checkProductColorConflict(ProductColor productColor) {
        if (productColorRepository.existsByProductAndColor(productColor.getProduct(), productColor.getColor())) {
            logger.error("ProductColor with Product ID: {} and Color ID: {} already exists",
                    productColor.getProduct().getProductId(), productColor.getColor().getColorId());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO<>(null,
                            "ProductColor with Product ID: " + productColor.getProduct().getProductId() + " already exists"));
        }
        return null;
    }

}
