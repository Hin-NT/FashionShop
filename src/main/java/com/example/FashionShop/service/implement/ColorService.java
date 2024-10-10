package com.example.FashionShop.service.implement;

import com.example.FashionShop.dto.ColorDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Color;
import com.example.FashionShop.repository.ColorRepository;
import com.example.FashionShop.service.interfaces.IColor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ColorService implements IColor {

    private static final Logger logger = LoggerFactory.getLogger(ColorService.class);

    private final ColorRepository colorRepository;

    @Override
    public ResponseEntity<ResponseDTO<List<ColorDTO>>> getAll() {
        try {
            List<Color> colors = colorRepository.findAll();
            if (colors.isEmpty()) {
                logger.info("No colors found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ResponseDTO<>(null, "No colors found"));
            }
            List<ColorDTO> colorDTOs = colors.stream()
                    .map(color -> new ColorDTO(color, 0))
                    .toList();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(colorDTOs, "Colors retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while fetching colors: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null, "Error occurred while fetching colors"));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Color>> create(Color color) {

        LocalDateTime currentDateTime = LocalDateTime.now();
        color.setCreatedAt(currentDateTime);
        color.setUpdatedAt(currentDateTime);

        if (color.getColorId() != null && colorRepository.existsById(color.getColorId())) {
            logger.warn("Color with ID: {} already exists", color.getColorId());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO<>(null,
                            "Color with ID: " + color.getColorId() + " already exists"));
        }

        try {
            Color colorSaved = colorRepository.save(color);
            logger.info("Color created successfully with ID: {}", colorSaved);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO<>(colorSaved, "Color created successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while creating color: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while creating color: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<ColorDTO>> findById(Color color) {

        if (color == null || color.getColorId() == null) {
            logger.warn("Color or ColorId is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Color or ColorId is null"));
        }

        try {
            Optional<Color> colorOptional = colorRepository.findById(color.getColorId());
            if (colorOptional.isPresent()) {
                logger.info("Color found with ID: {}", color.getColorId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(new ColorDTO(colorOptional.get(), 1),
                                "Color found with ID: " + color.getColorId()));
            } else {
                logger.info("Color not found with ID: {}", color.getColorId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null,
                                "Color not found with ID: " + color.getColorId()));
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching color: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while fetching color: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Color>> update(Color color) {

        String colorId = color.getColorId();

        if (!colorRepository.existsById(colorId)) {
            logger.warn("Color with ID: {} does not exist", colorId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO<>(null, "Color with ID: " + colorId + " does not exist"));
        }

        try {
            color.setUpdatedAt(LocalDateTime.now());
            Color colorUpdated = colorRepository.save(color);
            logger.info("Color updated successfully with ID: {}", colorId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(colorUpdated, "Color updated successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while updating color: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while updating color: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<String>> delete(Color color) {
        if (color == null || color.getColorId() == null) {
            logger.warn("Color or ColorId is null when deleting color");
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(null, "Color or ColorId is null"));
        }

        try {
            String colorId = color.getColorId();
            if (colorRepository.existsById(colorId)) {
                colorRepository.deleteById(colorId);
                logger.info("Color deleted successfully with ID: {}", colorId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body((new ResponseDTO<>(null, "Color deleted successfully")));
            } else {
                logger.warn("Color with ID: {} not found", colorId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null, "Color not found with ID: " + colorId));
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting color: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while deleting color: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<List<ColorDTO>>> findColorByColorName(String colorName) {
        try {
            List<Color> colors = colorRepository.findColorsByColorName(colorName);
            if (colors.isEmpty()) {
                logger.info("Color with name {} not found", colorName);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(null, "Color with name " + colorName + " not found"));
            }
            List<ColorDTO> colorsDTO = this.convertEntityToDTO(colors);
            logger.info("Color with name {} found", colorName);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(colorsDTO, "Colors retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while fetching color: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while fetching color: " + e.getMessage()));
        }
    }

    private List<ColorDTO> convertEntityToDTO(List<Color> colors) {
        return colors.stream()
                .map(color -> new ColorDTO(color, 0))
                .toList();
    }
}
