package com.camping_rental.server.domain.rental_order_info.vo;

import com.camping_rental.server.domain.rental_order_info.entity.RentalOrderInfoEntity;
import com.camping_rental.server.domain.rental_order_info.projection.RentalOrderInfoProjection;
import com.camping_rental.server.domain.rental_order_product.vo.RentalOrderProductVo;
import com.camping_rental.server.domain.room.vo.RoomVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalOrderInfoVo {
    private Long cid;
    private UUID id;
    private String orderNumber;
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
    private String csMemo;
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
        private String orderNumber;
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
        private String csMemo;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime createdAt;
        private UUID ordererId;
        private UUID lenderRoomId;

        public static Basic toVo(RentalOrderInfoEntity entity) {
            Basic vo = Basic.builder()
                    .id(entity.getId())
                    .orderNumber(entity.getOrderNumber())
                    .orderer(entity.getOrderer())
                    .ordererPhoneNumber(entity.getOrdererPhoneNumber())
                    .pickupDate(entity.getPickupDate())
                    .pickupTime(entity.getPickupTime())
                    .pickupPlace(entity.getPickupPlace())
                    .returnDate(entity.getReturnDate())
                    .returnTime(entity.getReturnTime())
                    .returnPlace(entity.getReturnPlace())
                    .ordererType(entity.getOrdererType())
                    .csMemo(entity.getCsMemo())
                    .createdAt(entity.getCreatedAt())
                    .ordererId(entity.getOrdererId())
                    .lenderRoomId(entity.getLenderRoomId())
                    .build();
            return vo;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FullJoin {
        private UUID id;
        private String orderNumber;
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
        private String csMemo;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime createdAt;
        private UUID ordererId;
        private UUID lenderRoomId;

        private RoomVo.Basic room;
        private Set<RentalOrderProductVo.Basic> rentalOrderProducts;

        public static FullJoin toVo(RentalOrderInfoProjection.FullJoin proj) {
            FullJoin vo = FullJoin.builder()
                    .id(proj.getRentalOrderInfoEntity().getId())
                    .orderNumber(proj.getRentalOrderInfoEntity().getOrderNumber())
                    .orderer(proj.getRentalOrderInfoEntity().getOrderer())
                    .ordererPhoneNumber(proj.getRentalOrderInfoEntity().getOrdererPhoneNumber())
                    .pickupDate(proj.getRentalOrderInfoEntity().getPickupDate())
                    .pickupTime(proj.getRentalOrderInfoEntity().getPickupTime())
                    .pickupPlace(proj.getRentalOrderInfoEntity().getPickupPlace())
                    .returnDate(proj.getRentalOrderInfoEntity().getReturnDate())
                    .returnTime(proj.getRentalOrderInfoEntity().getReturnTime())
                    .returnPlace(proj.getRentalOrderInfoEntity().getReturnPlace())
                    .ordererType(proj.getRentalOrderInfoEntity().getOrdererType())
                    .csMemo(proj.getRentalOrderInfoEntity().getCsMemo())
                    .createdAt(proj.getRentalOrderInfoEntity().getCreatedAt())
                    .ordererId(proj.getRentalOrderInfoEntity().getOrdererId())
                    .lenderRoomId(proj.getRentalOrderInfoEntity().getLenderRoomId())
                    .room(RoomVo.Basic.toVo(proj.getRoomEntity()))
                    .rentalOrderProducts(
                            proj.getRentalOrderProductEntities().stream().map(r->{
                                return RentalOrderProductVo.Basic.toVo(r);
                            }).collect(Collectors.toSet())
                    )
                    .build();
            return vo;
        }
    }
}
