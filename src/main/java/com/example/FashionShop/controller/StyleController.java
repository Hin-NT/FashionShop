package com.example.FashionShop.controller;

import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.dto.StyleDTO;
import com.example.FashionShop.model.Style;
import com.example.FashionShop.service.interfaces.IStyle;
import com.example.FashionShop.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/styles")
public class StyleController {

    private final IStyle styleService;

    @GetMapping("")
    public ResponseEntity<ResponseDTO<List<StyleDTO>>> getAllStyles() {
        return styleService.getAll();
    }

    @GetMapping("/{styleId}")
    public ResponseEntity<ResponseDTO<StyleDTO>> getStyleById(@PathVariable String styleId) {
        Style style = new Style();
        style.setStyleId(styleId);

        return styleService.findById(style);
    }

    @PostMapping("")
    public ResponseEntity<ResponseDTO<Style>> createStyle(@RequestBody Style style) {
        Util.trimFields(style);
        return styleService.create(style);
    }

    @PutMapping("")
    public ResponseEntity<ResponseDTO<Style>> updateStyle(@RequestBody Style style) {
        Util.trimFields(style);
        return styleService.update(style);
    }

    @DeleteMapping("/{styleId}")
    public ResponseEntity<ResponseDTO<String>> deleteStyle(@PathVariable String styleId) {
        Style style = new Style();
        style.setStyleId(styleId);
        return styleService.delete(style);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<StyleDTO>> findStyleByStyleName(@RequestParam String styleName) {
        if (styleName == null || styleName.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return styleService.findStyleByStyleName(styleName.trim());
    }
}
