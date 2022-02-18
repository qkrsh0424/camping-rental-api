package com.camping_rental.server.domain.item_option.entity;

import com.camping_rental.server.domain.item_option.dto.ItemOptionDto;
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
@Table(name = "item_option")
public class ItemOptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Integer price;

    @Column(name = "item_id")
    @Type(type = "uuid-char")
    private UUID itemId;

    public static ItemOptionEntity toEntity(ItemOptionDto dto) {
        ItemOptionEntity entity = ItemOptionEntity.builder()
                .cid(dto.getCid())
                .id(dto.getId())
                .name(dto.getName())
                .price(dto.getPrice())
                .itemId(dto.getItemId())
                .build();
        return entity;
    }
}
