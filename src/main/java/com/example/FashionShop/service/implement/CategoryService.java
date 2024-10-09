package com.example.FashionShop.service.implement;

import com.example.FashionShop.dto.CategoryDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Category;
import com.example.FashionShop.repository.CategoryRepository;
import com.example.FashionShop.service.interfaces.ICategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategory {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<ResponseDTO<List<CategoryDTO>>> getAll() {
        try {
            List<Category> categories = categoryRepository.findAll();
            if (categories.isEmpty()) {
                logger.info("No categories found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ResponseDTO<>(null, "No Categories found"));
            }
            List<CategoryDTO> categoryDTOs = categories.stream()
                    .map(category -> new CategoryDTO(category, 0))
                    .toList();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(categoryDTOs, "Categories retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while fetching categories: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while fetching categories: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Category>> create(Category category) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        category.setCreatedAt(currentDateTime);
        category.setUpdatedAt(currentDateTime);

        if (category.getCategoryId() != null && categoryRepository.existsById(category.getCategoryId())) {
            logger.warn("Category with ID: {} already exists", category.getCategoryId());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO<>(null,
                            "Category with ID: " + category.getCategoryId() + " already exists"));
        }

        try {
            categoryRepository.save(category);
            logger.info("Category created successfully with ID: {}", category.getCategoryId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO<>(category, "Category created successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while creating category: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while creating category: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<CategoryDTO>> findById(Category category) {

        if (category == null || category.getCategoryId() == null) {
            logger.warn("Category or categoryId is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Category or categoryId is null"));
        }

        try {
            Optional<Category> categoryOptional = categoryRepository.findById(category.getCategoryId());
            if (categoryOptional.isPresent()) {
                logger.info("Category found with ID: {}", category.getCategoryId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(new CategoryDTO(categoryOptional.get(), 1),
                                "Category found with ID: " + category.getCategoryId()));
            } else {
                logger.info("Category not found with ID: {}", category.getCategoryId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null,
                                "Category not found with ID: " + category.getCategoryId()));
            }
        } catch (Exception e) {
            logger.error("Error occurred while finding category: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while finding category: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Category>> update(Category category) {

        String categoryId = category.getCategoryId();

        if (!categoryRepository.existsById(categoryId)) {
            logger.warn("Category with ID: {} does not exist", categoryId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO<>(null,
                            "Category with ID: " + categoryId + " does not exist"));
        }

        try {
            category.setUpdatedAt(LocalDateTime.now());
            categoryRepository.save(category);
            logger.info("Category updated successfully with ID: {}", categoryId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(category,
                            "Category updated successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while updating category: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while updating category: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<String>> delete(Category category) {
        if (category == null || category.getCategoryId() == null) {
            logger.warn("Category or categoryId is null when deleting category");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Category or categoryId is null"));
        }

        try {
            if (categoryRepository.existsById(category.getCategoryId())) {
                categoryRepository.deleteById(category.getCategoryId());
                logger.info("Category deleted successfully with ID: {}", category.getCategoryId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(null, "Category deleted successfully"));
            } else {
                logger.warn("Category not found with ID: {}", category.getCategoryId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null,
                                "Category not found with ID: " + category.getCategoryId()));
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting category: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while deleting category: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<CategoryDTO>> findCategoryByCategoryName(String categoryName) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findCategoryByCategoryName(categoryName);

            if (optionalCategory.isPresent()) {
                logger.info("Category found with name: {}", categoryName);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(new CategoryDTO(optionalCategory.get(), 0),
                                "Category found with name: " + categoryName));
            } else {
                logger.info("Category not found with name: {}", categoryName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null, "Category not found with name: " + categoryName));
            }
        } catch (Exception e) {
            logger.error("Error occurred while finding category: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while finding category: " + e.getMessage()));
        }
    }

}