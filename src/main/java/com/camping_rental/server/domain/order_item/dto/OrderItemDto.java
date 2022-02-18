package com.camping_rental.server.domain.order_item.dto;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {
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
}
