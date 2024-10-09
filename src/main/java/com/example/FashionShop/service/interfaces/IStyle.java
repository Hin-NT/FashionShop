package com.example.FashionShop.service.interfaces;

import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.dto.StyleDTO;
import com.example.FashionShop.model.Style;
import org.springframework.http.ResponseEntity;

public interface IStyle extends IService<Style, StyleDTO> {

    ResponseEntity<ResponseDTO<StyleDTO>> findStyleByStyleName(String styleName);
}
