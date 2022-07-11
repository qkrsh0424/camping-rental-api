package com.camping_rental.server.domain.rental_order_info.vo;

import com.camping_rental.server.domain.rental_order_info.entity.RentalOrderInfoEntity;
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
public class RentalOrderInfoVo {
    private Long cid;
    private UUID id;
    private String orderer;
    private String ordererPhoneNumber;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime pickupDate;
    private String pickupTime;
    private String pickupPlace;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime returnDate;
    private String returnTime;
    private String returnPlace;
    private String ordererType;
    private String serviceAgreementYn;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updatedAt;
    private boolean deletedFlag;
    private UUID ordererId;
    private UUID lenderRoomId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Basic {
        private UUID id;
        private String orderer;
        private String ordererPhoneNumber;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime pickupDate;
        private String pickupTime;
        private String pickupPlace;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime returnDate;
        private String returnTime;
        private String returnPlace;
        private String ordererType;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime createdAt;
        private UUID ordererId;
        private UUID lenderRoomId;

        public static Basic toVo(RentalOrderInfoEntity entity) {
            Basic vo = Basic.builder()
                    .id(entity.getId())
                    .orderer(entity.getOrderer())
                    .ordererPhoneNumber(entity.getOrdererPhoneNumber())
                    .pickupDate(entity.getPickupDate())
                    .pickupTime(entity.getPickupTime())
                    .pickupPlace(entity.getPickupPlace())
                    .returnDate(entity.getReturnDate())
                    .returnTime(entity.getReturnTime())
                    .returnPlace(entity.getReturnPlace())
                    .ordererType(entity.getOrdererType())
                    .createdAt(entity.getCreatedAt())
                    .ordererId(entity.getOrdererId())
                    .lenderRoomId(entity.getLenderRoomId())
                    .build();
            return vo;
        }
    }
}
