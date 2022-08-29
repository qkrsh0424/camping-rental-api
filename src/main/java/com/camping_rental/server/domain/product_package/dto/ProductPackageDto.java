package com.camping_rental.server.domain.product_package.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.UUID;

public class ProductPackageDto {
    private Long cid;
    private UUID id;
    private String name;
    private Integer unit;
    private String thumbnailUri;
    private LocalDateTime updatedAt;
    private boolean deletedFlag;
    private UUID productId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Create{
        private UUID id;
        private String name;
        private Integer unit;
        private String thumbnailUri;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Update{
        private UUID id;
        private String name;
        private Integer unit;
        private String thumbnailUri;
    }
}
