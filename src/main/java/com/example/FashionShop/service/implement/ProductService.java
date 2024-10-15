package com.example.FashionShop.service.implement;

import com.example.FashionShop.dto.ProductDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Product;
import com.example.FashionShop.model.Style;
import com.example.FashionShop.repository.CategoryRepository;
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

    private final CategoryRepository categoryRepository;


    @Override
    public ResponseEntity<ResponseDTO<List<ProductDTO>>> findProductByProductName(String productName) {
        try {
            List<Product> products = productRepository.findProductsByProductName(productName);
            if (products.isEmpty()) {
                logger.info("No product found with name {}", productName);
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ResponseDTO<>(null, "No product found with name " + productName));
            }
            List<ProductDTO> productDTOs = this.convertProductToProductDTO(products);
            logger.info("Product found with name {}", productName);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(productDTOs, "Product found with name " + productName));
        } catch (Exception e) {
            logger.error("Error occurred while finding product by product name", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while finding product by product name"));
        }
    }

    private List<ProductDTO> convertProductToProductDTO(List<Product> products) {
        return products.stream().map(product -> new ProductDTO(product, 0)).toList();
    }

    @Override
    public ResponseEntity<ResponseDTO<List<ProductDTO>>> getAll() {
        try {

            List<Product> products = productRepository.findAll();
            if (products.isEmpty()) {
                logger.info("No product found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ResponseDTO<>(null, "No product found"));
            }
            List<ProductDTO> productDTOs = this.convertProductToProductDTO(products);
            logger.info("Product found");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(productDTOs, "Product found"));
        } catch (Exception e) {
            logger.error("Error occurred while finding products");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while finding products" + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Product>> create(Product product) {

        if (product == null || product.getProductName() == null || product.getProductName().isBlank()) {
            logger.warn("Product name is null or empty or blank");
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ResponseDTO<>(null, "Product name is null or empty or blank"));
        }

        if (!categoryRepository.existsById(product.getCategory().getCategoryId())) {
            logger.warn("Category id {} does not exist", product.getCategory().getCategoryId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Category id " + product.getCategory().getCategoryId() + " does not exist"));
        }

        if (!styleRepository.existsById(product.getStyle().getStyleId())) {
            logger.warn("Style id {} does not exist", product.getStyle().getStyleId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Style id " + product.getStyle().getStyleId() + " does not exist"));
        }

        String productName = Util.createIdForEntity(product.getProductName());
        product.setProductId(productName);

        LocalDateTime currentDateTime = LocalDateTime.now();
        product.setCreatedAt(currentDateTime);
        product.setUpdatedAt(currentDateTime);

        try {
            productRepository.save(product);
            logger.info("Product created successfully");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO<>(product, "Product created successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while creating product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while creating product" + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<ProductDTO>> findById(Product product) {

        if (product == null || product.getProductId() == null) {
            logger.info("Product id is null or empty");
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ResponseDTO<>(null, "Product id is null or empty"));
        }

        try {
            Optional<Product> productOptional = productRepository.findById(product.getProductId());
            if (productOptional.isPresent()) {
                logger.info("Product found with id {}", product.getProductId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(new ProductDTO(productOptional.get(), 0),
                                "Product found with id " + product.getProductId()));
            }
            logger.info("Product not found with id {}", product.getProductId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO<>(null, "Product id not found"));
        } catch (Exception e) {
            logger.error("Error occurred while finding product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while finding product" + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Product>> update(Product product) {

        if (product == null || product.getProductId() == null) {
            logger.info("Product id is null or empty");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseDTO<>(null, "Product id is null or empty"));
        }

        if (!categoryRepository.existsById(product.getCategory().getCategoryId())) {
            logger.warn("Category id {} does not exist", product.getCategory().getCategoryId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ResponseDTO<>(null, "Category id " + product.getCategory().getCategoryId() + " does not exist"));
        }

        if (styleRepository.existsById(product.getStyle().getStyleId())) {
            logger.warn("Style id {} does not exist", product.getStyle().getStyleId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ResponseDTO<>(null, "Style id " + product.getStyle().getStyleId() + " does not exist"));
        }

        try {
            Optional<Product> productOptional = productRepository.findById(product.getProductId());
            if (productOptional.isPresent()) {

                product.setUpdatedAt(LocalDateTime.now());

                productRepository.save(product);
                logger.info("Product updated successfully with id {}", product.getProductId());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(product, "Product updated successfully with id " + product.getProductId()));
            }
            logger.info("Product not found with id {}", product.getProductId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO<>(null, "Product id not found"));
        } catch (Exception e) {
            logger.error("Error occurred while updating product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>(null, "Error occurred while updating product" + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<String>> delete(Product product) {
        if (product == null || product.getProductId() == null) {
            logger.info("Product id is null or empty");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseDTO<>(null, "Product id is null or empty"));
        }

        try {
            Optional<Product> productOptional = productRepository.findById(product.getProductId());
            if (productOptional.isPresent()) {
                productRepository.deleteById(product.getProductId());
                logger.info("Product found with id {}", product.getProductId());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>("Product deleted successfully", "Product deleted successfully"));
            }
            logger.info("Product not found with id {}", product.getProductId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO<>(null, "Product not found"));
        } catch (Exception e) {
            logger.error("Error occurred while deleting product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>(null, "Error occurred while deleting product" + e.getMessage()));
        }
    }

}
