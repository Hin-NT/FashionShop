package com.example.FashionShop.controller;

import com.example.FashionShop.model.ProductColor;
import com.example.FashionShop.service.implement.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadImage(@RequestParam("productColorId") String ProductId, @RequestParam("files") List<MultipartFile> files) throws Exception {
        ProductColor productColor = new ProductColor(ProductId);
        return imageService.handleImage(productColor, files);
    }
}

