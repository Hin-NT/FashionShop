package com.example.FashionShop.service.implement;

import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.dto.StyleDTO;
import com.example.FashionShop.model.Style;
import com.example.FashionShop.repository.StyleRepository;
import com.example.FashionShop.service.interfaces.IStyle;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StyleService implements IStyle {

    private final Logger logger = LoggerFactory.getLogger(StyleService.class);
    private final StyleRepository styleRepository;

    @Override
    public ResponseEntity<ResponseDTO<List<StyleDTO>>> getAll() {
        try {
            List<Style> styles = styleRepository.findAll();
            if (styles.isEmpty()) {
                logger.info("No Styles found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseDTO<>(null, "No Styles found"));
            }
            List<StyleDTO> styleDTOs = this.convertEntityToDTO(styles);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(styleDTOs, "Styles retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while fetching styles: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>(null, "Error occurred while fetching styles: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Style>> create(Style style) {

        LocalDateTime currentDateTime = LocalDateTime.now();
        style.setCreatedAt(currentDateTime);
        style.setUpdatedAt(currentDateTime);

        if (style.getStyleName() == null || style.getStyleName().isEmpty()) {
            logger.error("Style name must not be null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(null, "Style name must not be null or empty"));
        }

        try {
            Style styleSaved = styleRepository.save(style);
            logger.info("Style created successfully: {}", style.getStyleId());
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(styleSaved, "Style created successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while creating style {}", style.getStyleId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>(null, "Error occurred while creating style: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<StyleDTO>> findById(Style style) {
        if (style == null || style.getStyleId() == null) {
            logger.info("Style or StyleId is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(null, "Style or StyleId is null"));
        }

        try {
            Optional<Style> styleOptional = styleRepository.findById(style.getStyleId());
            if (styleOptional.isPresent()) {
                logger.info("Style found with ID: {}", style.getStyleId());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(new StyleDTO(styleOptional.get(), 1), "Style found with ID: " + style.getStyleId()));
            } else {
                logger.info("Style not found with ID: {}", style.getStyleId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO<>(null, "Style not found with ID: " + style.getStyleId()));
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching style {}", style.getStyleId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>(null, "Error occurred while fetching style: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Style>> update(Style style) {
        if (style == null || style.getStyleId() == null) {
            logger.warn("Style or StyleId is null when updating style");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(null, "Style or StyleId is null"));
        }

        String styleId = style.getStyleId();

        if (!styleRepository.existsById(style.getStyleId())) {
            logger.info("Style with ID: {} does not exist", styleId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO<>(null, "Style with ID " + styleId + " does not exist"));
        }

        try {
            style.setUpdatedAt(LocalDateTime.now());

            Style styleUpdated = styleRepository.save(style);
            logger.info("Style updated successfully with ID: {}", styleId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(styleUpdated, "Style updated successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while updating style {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>(null, "Error occurred while updating style: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<String>> delete(Style style) {
        if (style == null || style.getStyleId() == null) {
            logger.warn("Style or StyleId is null when deleting style");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(null, "Style or StyleId is null"));
        }

        try {
            if (styleRepository.existsById(style.getStyleId())) {
                styleRepository.deleteById(style.getStyleId());
                logger.info("Style with ID {} deleted successfully", style.getStyleId());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(null, "Style deleted successfully"));
            } else {
                logger.warn("Style with ID {} not found", style.getStyleId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO<>(null, "Style not found with ID: " + style.getStyleId()));
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting style with ID: {}: {}", style.getStyleId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>(null, "Error occurred while deleting style: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<List<StyleDTO>>> findStyleByStyleName(String styleName) {
        try {
            List<Style> styles = styleRepository.findStylesByStyleName(styleName);
            if (styles.isEmpty()) {
                logger.info("Style with name {} not found", styleName);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(null, "Style with name " + styleName + " not found"));
            }
            List<StyleDTO> styleDTOs = this.convertEntityToDTO(styles);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(styleDTOs, "Style with name " + styleName + " found"));
        } catch (Exception e) {
            logger.error("Error occurred while fetching style {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>(null, "Error occurred while fetching style: " + e.getMessage()));
        }
    }

    private List<StyleDTO> convertEntityToDTO(List<Style> styles) {
        return styles.stream()
                .map(style -> new StyleDTO(style, 0))
                .toList();
    }
}
