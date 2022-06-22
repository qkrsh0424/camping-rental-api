package com.camping_rental.server.domain.region.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegionDto {
    private Long cid;
    private UUID id;
    private String sido;
    private String sigungu;
    private String jibunAddress;
    private String roadAddress;
    private String buildingName;
    private String address;
    private String addressDetail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deletedFlag;
    private UUID createdBy;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Create{
        private String sido;
        private String sigungu;
        private String jibunAddress;
        private String roadAddress;
        private String buildingName;
        private String address;
        private String addressDetail;
    }
}
