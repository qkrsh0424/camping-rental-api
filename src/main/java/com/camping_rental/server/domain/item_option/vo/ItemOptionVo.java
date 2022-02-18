package com.camping_rental.server.domain.item_option.vo;

import com.camping_rental.server.domain.item_option.entity.ItemOptionEntity;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemOptionVo {
    private UUID id;
    private String name;
    private Integer price;
    private UUID itemId;

    public static ItemOptionVo toVo(ItemOptionEntity entity) {
        ItemOptionVo vo = ItemOptionVo.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .itemId(entity.getItemId())
                .build();
        return vo;
    }
}
