package com.camping_rental.server.domain.order_info.vo;

import com.camping_rental.server.domain.order_info.entity.OrderInfoEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfoVo {
    private UUID id;
    private String orderer;
    private String ordererPhoneNumber;
    private LocalDateTime pickupDate;
    private LocalDateTime returnDate;
    private String pickupRegion;
    private String returnRegion;
    private String pickupTime;
    private String returnTime;
    private String serviceAgreementYn;
    private LocalDateTime createdAt;

    public static OrderInfoVo toVo(OrderInfoEntity entity) {
        OrderInfoVo vo = OrderInfoVo.builder()
                .id(entity.getId())
                .orderer(entity.getOrderer())
                .ordererPhoneNumber(entity.getOrdererPhoneNumber())
                .pickupDate(entity.getPickupDate())
                .returnDate(entity.getReturnDate())
                .pickupRegion(entity.getPickupRegion())
                .returnRegion(entity.getReturnRegion())
                .pickupTime(entity.getPickupTime())
                .returnTime(entity.getReturnTime())
                .serviceAgreementYn(entity.getServiceAgreementYn())
                .createdAt(entity.getCreatedAt())
                .build();
        return vo;
    }

}
