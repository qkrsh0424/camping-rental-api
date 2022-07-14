package com.camping_rental.server.domain.rental_order_product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalOrderProductDto {
    private Long cid;
    private UUID id;
    private String status;
    private String productName;
    private String thumbnailUri;
    private Integer price;
    private Integer discountMinimumHour;
    private Integer discountRate;
    private Integer unit;
    private boolean deletedFlag;
    private UUID rentalOrderInfoId;
    private UUID productId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Create{
        private String productName;
        private String thumbnailUri;
        private Integer price;
        private Integer discountMinimumHour;
        private Integer discountRate;
        private Integer unit;
        private UUID productId;
    }
}
