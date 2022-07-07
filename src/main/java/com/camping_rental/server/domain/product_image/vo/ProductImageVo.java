package com.camping_rental.server.domain.product_image.vo;

import com.camping_rental.server.domain.product_image.entity.ProductImageEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageVo {
    private Long cid;
    private UUID id;
    private String fileOriginName;
    private String fileStorageUri;
    private String fileFullUri;
    private String serviceUrl;
    private String filePath;
    private String fileExtension;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime madeAt;
    private Long size;
    private boolean deletedFlag;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;
    private UUID productId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Basic {
        private UUID id;
        private String fileOriginName;
        private String fileStorageUri;
        private String fileFullUri;
        private String serviceUrl;
        private String filePath;
        private String fileExtension;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime madeAt;
        private Long size;
        private UUID productId;

        public static Basic toVo(ProductImageEntity entity) {
            Basic vo = Basic.builder()
                    .id(entity.getId())
                    .fileOriginName(entity.getFileOriginName())
                    .fileStorageUri(entity.getFileStorageUri())
                    .fileFullUri(entity.getFileFullUri())
                    .serviceUrl(entity.getServiceUrl())
                    .filePath(entity.getFilePath())
                    .fileExtension(entity.getFileExtension())
                    .madeAt(entity.getMadeAt())
                    .size(entity.getSize())
                    .productId(entity.getProductId())
                    .build();
            return vo;
        }
    }
}
