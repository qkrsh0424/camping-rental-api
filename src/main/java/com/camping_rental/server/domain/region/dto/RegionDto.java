package com.camping_rental.server.domain.region.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

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
    private String userSelectedAddress;
    private String addressDetail;
    private String fullAddress;
    private String bname;
    private String bname1;
    private String bname2;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deletedFlag;
    private UUID createdBy;
    private Integer roomCid;
    private UUID roomId;

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
        private String userSelectedAddress;
        private String addressDetail;
        private String bname;
        private String bname1;
        private String bname2;
    }
}
