package com.camping_rental.server.domain.item_option.dto;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemOptionDto {
    private Integer cid;
    private UUID id;
    private String name;
    private Integer price;
    private UUID itemId;
}
