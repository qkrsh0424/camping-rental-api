package com.camping_rental.server.domain.order_item.projection;

import com.camping_rental.server.domain.order_info.entity.OrderInfoEntity;
import com.camping_rental.server.domain.order_item.entity.OrderItemEntity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class OrderItemProjection {

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class M2OJ {
        private OrderItemEntity orderItemEntity;
        private OrderInfoEntity orderInfoEntity;
    }
}
