package com.camping_rental.server.domain.item.vo;

import com.camping_rental.server.domain.item.entity.ItemEntity;
import com.camping_rental.server.domain.item_option.vo.ItemOptionVo;
import lombok.*;

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
public class ItemVo {
    private UUID id;
    private String name;
    private String description;
    private String thumbnailOriginName;
    private String thumbnailName;
    private String thumbnailFullUri;
    private String thumbnailPath;
    private String rentalRegions;
    private String returnRegions;
    private Integer price;
    private Integer discountRate;
    private String displayYn;
    private UUID categoryId;
    private String categoryName;
    private LocalDateTime createdAt;

    public static ItemVo toVo(ItemEntity entity) {
        ItemVo vo = ItemVo.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .thumbnailOriginName(entity.getThumbnailOriginName())
                .thumbnailName(entity.getThumbnailName())
                .thumbnailFullUri(entity.getThumbnailFullUri())
                .thumbnailPath(entity.getThumbnailPath())
                .rentalRegions(entity.getRentalRegions())
                .returnRegions(entity.getReturnRegions())
                .price(entity.getPrice())
                .discountRate(entity.getDiscountRate())
                .displayYn(entity.getDisplayYn())
                .categoryId(entity.getCategoryId())
                .categoryName(entity.getCategoryName())
                .createdAt(entity.getCreatedAt())
                .build();
        return vo;
    }
}
