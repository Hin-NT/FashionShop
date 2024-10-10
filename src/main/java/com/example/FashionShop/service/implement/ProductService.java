package com.example.FashionShop.service.implement;

import com.example.FashionShop.dto.ColorDTO;
import com.example.FashionShop.dto.ProductDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Product;
import com.example.FashionShop.model.ProductColor;
import com.example.FashionShop.model.Style;
import com.example.FashionShop.repository.ProductRepository;
import com.example.FashionShop.repository.StyleRepository;
import com.example.FashionShop.service.interfaces.IProduct;
import com.example.FashionShop.utils.Util;
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
public class ProductService implements IProduct {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final StyleRepository styleRepository;

    @Override
    public ResponseEntity<ResponseDTO<List<ProductDTO>>> getAll() {
        try {
            List<Product> products = (List<Product>) productRepository.findAll();
            if (products.isEmpty()) {
                logger.info("No products found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ResponseDTO<>(null, "No products found"));
            }
            List<ProductDTO> productDTOs = this.convertEntityToDTO(products);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(productDTOs, "Products retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while fetching products: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null, "Error occurred while fetching products"));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Product>> create(Product product) {
        String productId = Util.createIdForEntity(product.getProductName());
        product.setProductId(productId);

        LocalDateTime currentDateTime = LocalDateTime.now();
        product.setCreatedAt(currentDateTime);
        product.setUpdatedAt(currentDateTime);

        try {
            Product savedProduct = productRepository.save(product);
            product.setProductId(savedProduct.getProductId());
            logger.info("Product created successfully with ID: {}", savedProduct.getProductId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO<>(product, "Product created successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while creating product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while creating product: " + e.getMessage()));
        }
    }


    @Override
    public ResponseEntity<ResponseDTO<ProductDTO>> findById(Product product) {
        if (product.getProductId() == null) {
            logger.warn("Product ID is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Product ID is null"));
        }

        try {
            Optional<Product> productOptional = productRepository.findById(product.getProductId());
            if (productOptional.isPresent()) {
                logger.info("Product found with ID: {}", product.getProductId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(new ProductDTO(productOptional.get(), 1),
                                "Product found successfully"));
            } else {
                logger.info("Product not found with ID: {}", product.getProductId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null,
                                "Product not found with ID: " + product.getProductId()));
            }
        } catch (Exception e) {
            logger.error("Error occurred while finding product: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while finding product: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Product>> update(Product product) {
        String productId = product.getProductId();

        if (!productRepository.existsById(productId)) {
            logger.warn("Product with ID: {} does not exist", productId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO<>(null,
                            "Product with ID: " + productId + " does not exist"));
        }

        try {
            Optional<Style> optionalStyle = Optional.ofNullable(product.getStyle())
                    .flatMap(style -> styleRepository.findById(style.getStyleId()));

            if (optionalStyle.isPresent()) {
                product.setStyle(optionalStyle.get());
            } else if (product.getStyle() != null) {
                logger.warn("Style with ID: {} not found", product.getStyle().getStyleId());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDTO<>(null, "Style not found"));
            }

            product.setUpdatedAt(LocalDateTime.now());

            Product updatedProduct = productRepository.save(product);
            logger.info("Product updated successfully with ID: {}", productId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(updatedProduct, "Product updated successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while updating product: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while updating product: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<String>> delete(Product product) {
        if (product == null || product.getProductId() == null) {
            logger.warn("Product or Product ID is null when deleting product");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Product or Product ID is null"));
        }

        try {
            if (productRepository.existsById(product.getProductId())) {
                productRepository.deleteById(product.getProductId());
                logger.info("Product with ID: {} deleted successfully", product.getProductId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(null, "Product deleted successfully"));
            } else {
                logger.warn("Product not found with ID: {}", product.getProductId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null,
                                "Product not found with ID: " + product.getProductId()));
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting product with ID: {}: {}", product.getProductId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while deleting product: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<List<ProductDTO>>> findProductByProductName(String productName) {
        try {
            List<Product> products = productRepository.findProductsByProductName(productName);
            if (products.isEmpty()) {
                logger.info("Product with name {} not found", productName);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(null, "Product with name " + productName + " not found"));
            }
            List<ProductDTO> productColors = this.convertEntityToDTO(products);
            logger.info("Product with name {} found", productName);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(productColors, "Products retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while finding product: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while finding product: " + e.getMessage()));
        }
    }

    private List<ProductDTO> convertEntityToDTO(List<Product> products) {
        return products.stream()
                .map(product -> new ProductDTO(product, 0))
                .toList();
    }
}
