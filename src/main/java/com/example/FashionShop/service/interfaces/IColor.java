package com.example.FashionShop.service.interfaces;

import com.example.FashionShop.dto.ColorDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Color;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IColor extends IService<Color, ColorDTO> {
    ResponseEntity<ResponseDTO<List<ColorDTO>>> findColorByColorName(String colorName);
}
