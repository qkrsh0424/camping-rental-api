package com.camping_rental.server.domain.order_info.vo;

import com.camping_rental.server.domain.order_info.entity.OrderInfoEntity;
import com.camping_rental.server.domain.order_info.projection.OrderInfoProjection;
import com.camping_rental.server.domain.order_item.entity.OrderItemEntity;
import com.camping_rental.server.domain.order_item.vo.OrderItemVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfoVo {
    private UUID id;
    private String status;
    private String orderer;
    private String ordererPhoneNumber;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime pickupDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime returnDate;
    private String pickupRegion;
    private String returnRegion;
    private String pickupTime;
    private String returnTime;
    private String serviceAgreementYn;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;

    public static OrderInfoVo toVo(OrderInfoEntity entity) {
        OrderInfoVo vo = OrderInfoVo.builder()
                .id(entity.getId())
                .status(entity.getStatus())
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

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderItem {
        private Integer cid;
        private UUID id;
        private String itemName;
        private String categoryName;
        private String thumbnailFullUri;
        private Integer price;
        private Integer discountRate;
        private Integer unit;
        private Integer nights;
        private Integer beforeDiscountPrice;
        private Integer afterDiscountPrice;
        private String adoptedDiscountYn;
        private UUID itemId;
        private UUID categoryId;
        private UUID orderInfoId;

        public static OrderItem toVo(OrderInfoProjection.OrderItemPO po) {
            OrderItem vo = OrderItem.builder()
                    .id(po.getId())
                    .itemName(po.getItemName())
                    .categoryName(po.getCategoryName())
                    .thumbnailFullUri(po.getThumbnailFullUri())
                    .price(po.getPrice())
                    .discountRate(po.getDiscountRate())
                    .unit(po.getUnit())
                    .nights(po.getNights())
                    .beforeDiscountPrice(po.getBeforeDiscountPrice())
                    .afterDiscountPrice(po.getAfterDiscountPrice())
                    .adoptedDiscountYn(po.getAdoptedDiscountYn())
                    .itemId(po.getItemId())
                    .build();
            return vo;
        }
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class WithOrderItems {
        private Integer cid;
        private UUID id;
        private String status;
        private String orderer;
        private String ordererPhoneNumber;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime pickupDate;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime returnDate;
        private String pickupRegion;
        private String returnRegion;
        private String pickupTime;
        private String returnTime;
        private String serviceAgreementYn;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime createdAt;

        private List<OrderItem> orderItems;

        public static WithOrderItems toVo(OrderInfoProjection.JoinOrderItems proj) {
            OrderInfoProjection.OrderInfoPO orderInfoPO = proj.getOrderInfoPO();
            List<OrderInfoProjection.OrderItemPO> orderItemPOs = proj.getOrderItemPOs();

            if (orderInfoPO == null) {
                return null;
            }

            WithOrderItems result = WithOrderItems.builder()
                    .id(orderInfoPO.getId())
                    .status(orderInfoPO.getStatus())
                    .orderer(orderInfoPO.getOrderer())
                    .ordererPhoneNumber(orderInfoPO.getOrdererPhoneNumber())
                    .pickupDate(orderInfoPO.getPickupDate())
                    .returnDate(orderInfoPO.getReturnDate())
                    .pickupRegion(orderInfoPO.getPickupRegion())
                    .returnRegion(orderInfoPO.getReturnRegion())
                    .pickupTime(orderInfoPO.getPickupTime())
                    .returnTime(orderInfoPO.getReturnTime())
                    .createdAt(orderInfoPO.getCreatedAt())
                    .build();

            if (!orderItemPOs.isEmpty()) {
                result.setOrderItems(
                        orderItemPOs.stream().map(r -> OrderItem.toVo(r)).collect(Collectors.toList())
                );
            }
            return result;
        }
    }
}
