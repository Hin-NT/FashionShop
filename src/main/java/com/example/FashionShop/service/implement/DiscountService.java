package com.example.FashionShop.service.implement;

import com.example.FashionShop.dto.DiscountDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.enums.DiscountStatus;
import com.example.FashionShop.model.Discount;
import com.example.FashionShop.repository.DiscountRepository;
import com.example.FashionShop.service.interfaces.IDiscount;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscountService implements IDiscount {

    private final Logger logger = LoggerFactory.getLogger(DiscountService.class);

    private final DiscountRepository discountRepository;

    @Override
    public ResponseEntity<ResponseDTO<List<DiscountDTO>>> getAll() {
        try {
            List<Discount> discounts = discountRepository.findAll();
            if (discounts.isEmpty()) {
                logger.info("No Discounts found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ResponseDTO<>(null, "No Discounts found"));
            }
            List<DiscountDTO> discountDTOs = discounts.stream()
                    .map(discount -> new DiscountDTO(discount, 0))
                    .toList();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(discountDTOs, "Discounts retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while fetching discounts: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null, "Error occurred while fetching discounts"));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Discount>> create(Discount discount) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        discount.setCreatedAt(currentDateTime);
        discount.setUpdatedAt(currentDateTime);

        try {
            if (isTimeOverlap(discount)) {
                logger.warn("Discount time overlaps with an existing discount");
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ResponseDTO<>(null,
                                "Discount time overlaps with an existing discount"));
            }

            if (discount.getStartTime() != null && discount.getEndTime() != null) {
                if (currentDateTime.isBefore(discount.getStartTime())) {
                    discount.setDiscountStatus(DiscountStatus.UPCOMING);
                } else if (currentDateTime.isAfter(discount.getEndTime())) {
                    discount.setDiscountStatus(DiscountStatus.EXPIRED);
                } else {
                    discount.setDiscountStatus(DiscountStatus.ACTIVE);
                }
            }

            Discount discountSaved = discountRepository.save(discount);
            logger.info("Discount created successfully with ID: {}", discountSaved.getDiscountId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO<>(discountSaved, "Discount created successfully"));

        } catch (Exception e) {
            logger.error("Error occurred while creating discount: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while creating discount" + e.getMessage()));
        }
    }

    private boolean isTimeOverlap(Discount discountToCheck, String... excludeId) {
        List<Discount> existingDiscounts = discountRepository.findAll();
        return existingDiscounts.stream().anyMatch(discount ->
                (excludeId.length == 0 || !discount.getDiscountId().equals(excludeId[0])) &&
                        ((discountToCheck.getStartTime().isBefore(discount.getEndTime()) || discountToCheck.getStartTime().isEqual(discount.getEndTime())) &&
                                (discountToCheck.getEndTime().isAfter(discount.getStartTime()) || discountToCheck.getEndTime().isEqual(discount.getStartTime())))
        );
    }

    @Override
    public ResponseEntity<ResponseDTO<DiscountDTO>> findById(Discount discount) {
        if (discount == null || discount.getDiscountId() == null) {
            logger.info("Discount or discount ID is null in findById");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Discount or discount ID is null"));
        }

        try {
            Optional<Discount> discountOptional = discountRepository.findById(discount.getDiscountId());
            if (discountOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(new DiscountDTO(discountOptional.get(), 1),
                                "Color found with ID: {}" + discountOptional.get().getDiscountId()));
            } else {
                logger.warn("Discount with ID: {} not found in findById", discount.getDiscountId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null, "Discount not found"));
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching discount: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null, "Error occurred while fetching discount"));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Discount>> update(Discount discount) {
        String discountId = discount.getDiscountId();

        if (discountId == null || discountId.isEmpty()) {
            logger.warn("Discount ID is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Discount ID is null"));
        }

        if (!discountRepository.existsById(discountId)) {
            logger.warn("Discount with ID: {} does not exist", discountId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO<>(null, "Discount not found"));
        }

        try {
            if (isTimeOverlap(discount, discountId)) {
                logger.warn("Updated discount time overlaps with an existing discount");
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ResponseDTO<>(null,
                                "Updated discount time overlaps with an existing discount"));
            }

            discount.setUpdatedAt(LocalDateTime.now());

            LocalDateTime currentDateTime = LocalDateTime.now();
            if (discount.getStartTime() != null && discount.getEndTime() != null) {
                if (currentDateTime.isBefore(discount.getStartTime())) {
                    discount.setDiscountStatus(DiscountStatus.UPCOMING);
                } else if (currentDateTime.isAfter(discount.getEndTime())) {
                    discount.setDiscountStatus(DiscountStatus.EXPIRED);
                } else {
                    discount.setDiscountStatus(DiscountStatus.ACTIVE);
                }
            }

            Discount discountUpdated = discountRepository.save(discount);
            logger.info("Discount updated successfully: {}", discountUpdated);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(discountUpdated, "Discount updated successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while updating discount: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null, "Error occurred while updating discount"));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<String>> delete(Discount discount) {
        if (discount == null || discount.getDiscountId() == null || discount.getDiscountId().isEmpty()) {
            logger.info("Discount or DiscountId is null or empty when deleting discount");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Discount or DiscountId is null or empty"));
        }

        try {
            if (discountRepository.existsById(discount.getDiscountId())) {
                discountRepository.deleteById(discount.getDiscountId());
                logger.info("Discount with ID: {} deleted successfully", discount.getDiscountId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(null, "Discount deleted successfully"));
            } else {
                logger.warn("Discount with ID: {} not found", discount.getDiscountId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null, "Discount not found"));
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting discount with ID: {}: {}", discount.getDiscountId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while deleting discount: " + e.getMessage()));
        }
    }

    @Override
    @Transactional
    @Scheduled(fixedRate = 12 * 60 * 60 * 1000)
    public void updateDiscountByDiscountStatus() {
        LocalDateTime today = LocalDateTime.now();

        List<Discount> discounts = discountRepository.findAll();
        for (Discount discount : discounts) {
            if (discount.getStartTime() != null && discount.getEndTime() != null) {
                if (discount.getStartTime().isBefore(today) && discount.getEndTime().isAfter(today)) {
                    if (discount.getDiscountStatus() != DiscountStatus.ACTIVE) {
                        discount.setDiscountStatus(DiscountStatus.ACTIVE);
                        discountRepository.save(discount);
                    }
                } else if (discount.getEndTime().isBefore(today)) {
                    if (discount.getDiscountStatus() != DiscountStatus.EXPIRED) {
                        discount.setDiscountStatus(DiscountStatus.EXPIRED);
                        discountRepository.save(discount);
                    }
                }
            }
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<DiscountDTO>> findDiscountByDiscountName(String discountName) {
        if (discountName == null || discountName.isEmpty()) {
            logger.info("Discount name is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Discount name is null or empty"));
        }
        try {
            Optional<Discount> discountOptional = discountRepository.findDiscountByDiscountName(discountName);
            if (discountOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO<>(new DiscountDTO(discountOptional.get(), 1),
                                "Discount found"));
            } else {
                logger.warn("Discount with name: {} not found", discountName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null,
                                "Discount with name '" + discountName + "' not found"));
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching discount: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while fetching discount: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<List<DiscountDTO>>> findDiscountByDiscountStatus(DiscountStatus discountStatus) {
        if (discountStatus == null) {
            logger.info("Discount status is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(null, "Discount status is null"));
        }

        try {
            List<Discount> discounts = discountRepository.findDiscountByDiscountStatus(discountStatus);
            if (discounts.isEmpty()) {
                logger.warn("No discounts found with status: {}", discountStatus);
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ResponseDTO<>(null,
                                "No discounts found with status: " + discountStatus));
            }

            List<DiscountDTO> discountDTOs = discounts.stream()
                    .map(discount -> new DiscountDTO(discount, 0))
                    .toList();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO<>(discountDTOs, "Discount found"));
        } catch (Exception e) {
            logger.error("Error occurred while fetching discounts by status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(null,
                            "Error occurred while fetching discounts: " + e.getMessage()));
        }
    }

}
