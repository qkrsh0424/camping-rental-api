package com.camping_rental.server.domain.region.vo;

import com.camping_rental.server.domain.region.entity.RegionEntity;
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
public class RegionVo {
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
    public static class Basic {
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

        public static Basic toVo(RegionEntity entity) {
            Basic vo = Basic.builder()
                    .id(entity.getId())
                    .sido(entity.getSido())
                    .sigungu(entity.getSigungu())
                    .jibunAddress(entity.getJibunAddress())
                    .roadAddress(entity.getRoadAddress())
                    .buildingName(entity.getBuildingName())
                    .address(entity.getAddress())
                    .userSelectedAddress(entity.getUserSelectedAddress())
                    .addressDetail(entity.getAddressDetail())
                    .fullAddress(entity.getFullAddress())
                    .bname(entity.getBname())
                    .bname1(entity.getBname1())
                    .bname2(entity.getBname2())
                    .build();
            return vo;
        }
    }
}
