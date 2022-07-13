package com.camping_rental.server.domain.product_image.dto;

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
public class ProductImageDto {
    private Long cid;
    private UUID id;
    private String fileName;
    private String fileOriginName;
    private String fileStorageUri;
    private String fileFullUri;
    private String serviceUrl;
    private String filePath;
    private String fileExtension;
    private LocalDateTime madeAt;
    private Long size;
    private boolean deletedFlag;
    private LocalDateTime createdAt;
    private UUID productId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Create{
        private UUID id;
        private String fileName;
        private String fileOriginName;
        private String fileStorageUri;
        private String fileFullUri;
        private String serviceUrl;
        private String filePath;
        private String fileExtension;
        private LocalDateTime madeAt;
        private Long size;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Update{
        private UUID id;
        private String fileName;
        private String fileOriginName;
        private String fileStorageUri;
        private String fileFullUri;
        private String serviceUrl;
        private String filePath;
        private String fileExtension;
        private LocalDateTime madeAt;
        private Long size;
    }
}
