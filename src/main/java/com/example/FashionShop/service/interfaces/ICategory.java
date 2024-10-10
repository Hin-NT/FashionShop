package com.example.FashionShop.service.interfaces;

import com.example.FashionShop.dto.CategoryDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICategory extends IService<Category, CategoryDTO> {
    ResponseEntity<ResponseDTO<List<CategoryDTO>>> findCategoryByCategoryName(String categoryName);
}
