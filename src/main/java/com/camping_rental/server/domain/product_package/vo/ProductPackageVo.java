package com.camping_rental.server.domain.product_package.vo;

import com.camping_rental.server.domain.product_package.entity.ProductPackageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProductPackageVo {
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
    public static class Basic {
        private UUID id;
        private String name;
        private Integer unit;
        private String thumbnailUri;
        private UUID productId;

        public static Basic toVo(ProductPackageEntity entity) {
            Basic vo = Basic.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .unit(entity.getUnit())
                    .thumbnailUri(entity.getThumbnailUri())
                    .productId(entity.getProductId())
                    .build();
            return vo;
        }
    }
}
