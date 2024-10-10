package com.example.FashionShop.controller;

import com.example.FashionShop.dto.CategoryDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Category;
import com.example.FashionShop.service.interfaces.ICategory;
import com.example.FashionShop.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final ICategory categoryService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<CategoryDTO>>> getAllCategories() {
        return categoryService.getAll();
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseDTO<CategoryDTO>> getCategoryById(@PathVariable String categoryId) {
        Category category = new Category();
        category.setCategoryId(categoryId);
        return categoryService.findById(category);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<List<CategoryDTO>>> getCategoryByCategoryName(@RequestParam String categoryName) {
        if (categoryName == null || categoryName.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(null, "CategoryName is required"));
        }
        return categoryService.findCategoryByCategoryName(categoryName.trim());
    }

    @PostMapping("")
    public ResponseEntity<ResponseDTO<Category>> createCategory(@RequestBody Category category) {
        Util.trimFields(category);
        return categoryService.create(category);
    }

    @PutMapping("")
    public ResponseEntity<ResponseDTO<Category>> updateCategory(@RequestBody Category category) {
        Util.trimFields(category);
        return categoryService.update(category);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ResponseDTO<String>> deleteCategory(@PathVariable String categoryId) {
        Category category = new Category();
        category.setCategoryId(categoryId);
        return categoryService.delete(category);
    }

}
