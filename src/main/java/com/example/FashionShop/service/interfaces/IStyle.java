package com.example.FashionShop.service.interfaces;

import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.dto.StyleDTO;
import com.example.FashionShop.model.Style;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IStyle extends IService<Style, StyleDTO> {

    ResponseEntity<ResponseDTO<List<StyleDTO>>> findStyleByStyleName(String styleName);
}
