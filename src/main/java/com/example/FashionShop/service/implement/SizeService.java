package com.example.FashionShop.service.implement;

import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.dto.SizeDTO;
import com.example.FashionShop.model.Size;
import com.example.FashionShop.repository.SizeRepository;
import com.example.FashionShop.service.interfaces.ISize;
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
public class SizeService implements ISize {

    private final Logger logger = LoggerFactory.getLogger(SizeService.class);

    private final SizeRepository sizeRepository;

    @Override
    public ResponseEntity<ResponseDTO<List<SizeDTO>>> getAll() {
        try {
            List<Size> sizes = sizeRepository.findAll();
            if (sizes.isEmpty()) {
                logger.info("No Sizes found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ResponseDTO<>(null, "No Sizes found"));
            }
            List<SizeDTO> sizeDTOs = sizes.stream()
                    .map(SizeDTO::new)
                    .toList();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(sizeDTOs, "Sizes retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while fetching sizes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null, "Error occurred while fetching sizes" + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Size>> create(Size size) {

        LocalDateTime currentDateTime = LocalDateTime.now();
        size.setCreatedAt(currentDateTime);
        size.setUpdatedAt(currentDateTime);

        if (sizeRepository.existsById(size.getSizeId())) {
            logger.info("Size with ID: {} already exists", size.getSizeId());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO<>(null, "Size with ID: " + size.getSizeId() + " already exists"));
        }

        try {
            Size sizeSaved = sizeRepository.save(size);
            logger.info("Size created successfully with ID: {}", size.getSizeId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO<>(sizeSaved, "Size created successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while creating size {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null, "Error occurred while creating size " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<SizeDTO>> findById(Size size) {
        if (size == null || size.getSizeId() == null) {
            logger.info("Size or SizeId is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Size or SizeId is null"));
        }
        try {
            Optional<Size> sizeOptional = sizeRepository.findById(size.getSizeId());

            if (sizeOptional.isPresent()) {
                logger.info("Size found with ID: {}", size.getSizeId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(new SizeDTO(sizeOptional.get()), "Size found successfully"));
            } else {
                logger.info("Size not found with ID: {}", size.getSizeId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null, "Size not found with ID: " + size.getSizeId()));
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching size: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null, "Error occurred while fetching size" + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Size>> update(Size size) {

        String sizeId = size.getSizeId();

        if (!sizeRepository.existsById(size.getSizeId())) {
            logger.info("Size with id {} already exists", sizeId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO<>(null, "Size with id " + size.getSizeId() + " already exists"));
        }

        try {
            size.setUpdatedAt(LocalDateTime.now());

            Size sizeUpdated = sizeRepository.save(size);
            logger.info("Size updated successfully with ID: {}", sizeId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(sizeUpdated, "Size updated successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while updating size {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null, "Error occurred while updating size " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<String>> delete(Size size) {
        if (size == null || size.getSizeId() == null) {
            logger.warn("Size or SizeId is null when deleting size");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Size or SizeId is null"));
        }

        try {
            if (sizeRepository.existsById(size.getSizeId())) {
                sizeRepository.deleteById(size.getSizeId());
                logger.info("Size with ID: {} deleted successfully", size.getSizeId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(null, "Deleted size successfully"));
            } else {
                logger.warn("Size with ID {} not found", size.getSizeId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null, "Size not found with ID: " + size.getSizeId()));
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting size with ID: {}: {}", size.getSizeId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null, "Error occurred while deleting size: " + e.getMessage()));
        }
    }

}
