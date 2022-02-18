package com.camping_rental.server.domain.order_item.entity;

import com.camping_rental.server.domain.order_item.dto.OrderItemDto;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_item")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "thumbnail_full_uri")
    private String thumbnailFullUri;

    @Column(name = "price")
    private Integer price;

    @Column(name = "discount_rate")
    private Integer discountRate;

    @Column(name = "unit")
    private Integer unit;

    @Column(name = "nights")
    private Integer nights;

    @Column(name = "before_discount_price")
    private Integer beforeDiscountPrice;

    @Column(name = "after_discount_price")
    private Integer afterDiscountPrice;

    @Column(name = "adopted_discount_yn")
    private String adoptedDiscountYn;

    @Column(name = "item_id")
    @Type(type = "uuid-char")
    private UUID itemId;

    @Column(name = "category_id")
    @Type(type = "uuid-char")
    private UUID categoryId;

    @Column(name = "order_info_id")
    @Type(type = "uuid-char")
    private UUID orderInfoId;

    public static OrderItemEntity toEntity(OrderItemDto dto) {
        OrderItemEntity entity = OrderItemEntity.builder()
                .cid(dto.getCid())
                .id(dto.getId())
                .itemName(dto.getItemName())
                .categoryName(dto.getCategoryName())
                .thumbnailFullUri(dto.getThumbnailFullUri())
                .price(dto.getPrice())
                .discountRate(dto.getDiscountRate())
                .unit(dto.getUnit())
                .nights(dto.getNights())
                .beforeDiscountPrice(dto.getBeforeDiscountPrice())
                .afterDiscountPrice(dto.getAfterDiscountPrice())
                .adoptedDiscountYn(dto.getAdoptedDiscountYn())
                .itemId(dto.getItemId())
                .categoryId(dto.getCategoryId())
                .orderInfoId(dto.getOrderInfoId())
                .build();
        return entity;
    }
}
