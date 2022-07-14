package com.camping_rental.server.domain.product.dto;

import com.camping_rental.server.domain.product_image.dto.ProductImageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long cid;
    private UUID id;
    private String name;
    private String description;
    private String thumbnailUri;
    private Integer price;
    private Integer minimumRentalHour;
    private String discountYn;
    private Integer discountMinimumHour;
    private Integer discountRate;
    private String displayYn;
    private boolean deletedFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID productCategoryId;
    private UUID roomId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Create{
        private String name;
        private String description;
        private Integer price;
        private Integer minimumRentalHour;
        private String discountYn;
        private Integer discountMinimumHour;
        private Integer discountRate;
        private UUID productCategoryId;
        private UUID roomId;
        private List<ProductImageDto.Create> productImages;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Update{
        private String name;
        private String description;
        private Integer price;
        private Integer minimumRentalHour;
        private String discountYn;
        private Integer discountMinimumHour;
        private Integer discountRate;
        private String displayYn;
        private UUID productCategoryId;
        private UUID roomId;
        private List<ProductImageDto.Update> productImages;
    }
}
