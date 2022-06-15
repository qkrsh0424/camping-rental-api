package com.camping_rental.server.domain.order_info.projection;

import com.camping_rental.server.domain.item.entity.ItemEntity;
import com.camping_rental.server.domain.order_info.entity.OrderInfoEntity;
import com.camping_rental.server.domain.order_item.entity.OrderItemEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfoProjection {
    private OrderInfoProjection.OrderInfoPO orderInfoPO;
    private List<OrderInfoProjection.OrderItemPO> orderItemPOs;

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class JoinOrderItems{
        private OrderInfoProjection.OrderInfoPO orderInfoPO;
        private List<OrderInfoProjection.OrderItemPO> orderItemPOs;
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderInfoPO{
        private UUID id;
        private String status;
        private String orderer;
        private String ordererPhoneNumber;
        private LocalDateTime pickupDate;
        private LocalDateTime returnDate;
        private String pickupRegion;
        private String returnRegion;
        private String pickupTime;
        private String returnTime;
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderItemPO{
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
    }
}
