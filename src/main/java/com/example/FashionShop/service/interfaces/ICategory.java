package com.example.FashionShop.service.interfaces;

import com.example.FashionShop.dto.CategoryDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Category;
import org.springframework.http.ResponseEntity;

public interface ICategory extends IService<Category, CategoryDTO> {

    ResponseEntity<ResponseDTO<CategoryDTO>> findCategoryByCategoryName(String categoryName);
}
