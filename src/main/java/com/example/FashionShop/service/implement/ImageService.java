package com.example.FashionShop.service.implement;

import com.example.FashionShop.model.Image;
import com.example.FashionShop.model.ProductColor;
import com.example.FashionShop.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final ImageRepository imageRepository;

    private final CloudinaryService cloudinaryService;

    public ResponseEntity<List<String>> handleImage(ProductColor productColor, List<MultipartFile> files) throws Exception {
        if (files == null || files.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        List<String> imagesUrl = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            try {
                validateImageFile(file);

                String imageUrl = cloudinaryService.uploadImage(file);
                logger.info("Processing file: {}", file.getOriginalFilename());

                Image image = Image.builder().productColor(productColor).imageUrl(imageUrl).build();
                imageRepository.save(image);
                imagesUrl.add(image.getImageUrl());
                logger.info("Successfully saved image: {}", imageUrl);
            } catch (Exception e) {
                logger.error("Error processing file: {}", file.getOriginalFilename(), e);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(imagesUrl);
    }

    private void validateImageFile(MultipartFile file) throws Exception {
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new Exception("File is too large! Maximum size is 10MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image")) {
            throw new Exception("Unsupported file type! Only images are allowed.");
        }
    }

}
