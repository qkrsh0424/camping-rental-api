package com.camping_rental.server.domain.order_item.vo;

import com.camping_rental.server.domain.order_item.entity.OrderItemEntity;
import lombok.*;
import org.hibernate.criterion.Order;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemVo {
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

    public static OrderItemVo toVo(OrderItemEntity entity) {
        OrderItemVo vo = OrderItemVo.builder()
                .id(entity.getId())
                .itemName(entity.getItemName())
                .categoryName(entity.getCategoryName())
                .thumbnailFullUri(entity.getThumbnailFullUri())
                .price(entity.getPrice())
                .discountRate(entity.getDiscountRate())
                .unit(entity.getUnit())
                .nights(entity.getNights())
                .beforeDiscountPrice(entity.getBeforeDiscountPrice())
                .afterDiscountPrice(entity.getAfterDiscountPrice())
                .adoptedDiscountYn(entity.getAdoptedDiscountYn())
                .itemId(entity.getItemId())
                .categoryId(entity.getCategoryId())
                .orderInfoId(entity.getOrderInfoId())
                .build();
        return vo;
    }
}
