package com.example.FashionShop.controller;

import com.example.FashionShop.dto.ColorDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Color;
import com.example.FashionShop.service.interfaces.IColor;
import com.example.FashionShop.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/colors")
public class ColorController {

    private final IColor colorService;

    @GetMapping("")
    public ResponseEntity<ResponseDTO<List<ColorDTO>>> getAllColors() {
        return colorService.getAll();
    }

    @GetMapping("/{colorId}")
    public ResponseEntity<ResponseDTO<ColorDTO>> getColorById(@PathVariable("colorId") String colorId) {
        Color color = new Color();
        color.setColorId(colorId);
        return colorService.findById(color);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<List<ColorDTO>>> searchColor(@RequestParam String colorName) {
        if(colorName == null || colorName.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(null, "ColorName is required"));
        }
        return colorService.findColorByColorName(colorName.trim());
    }

    @PostMapping("")
    public ResponseEntity<ResponseDTO<Color>> createColor(@RequestBody Color color) {
        Util.trimFields(color);
        return colorService.create(color);
    }

    @PutMapping("")
    public ResponseEntity<ResponseDTO<Color>> updateColor(@RequestBody Color color) {
        Util.trimFields(color);
        return colorService.update(color);
    }

    @DeleteMapping("/{colorId}")
    public ResponseEntity<ResponseDTO<String>> deleteColor(@PathVariable("colorId") String colorId) {
        Color color = new Color();
        color.setColorId(colorId);
        return colorService.delete(color);
    }

}
