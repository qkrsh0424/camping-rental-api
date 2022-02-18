package com.camping_rental.server.domain.item.entity;

import com.camping_rental.server.domain.item.dto.ItemDto;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "item")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "thumbnail_origin_name")
    private String thumbnailOriginName;

    @Column(name = "thumbnail_name")
    private String thumbnailName;

    @Column(name = "thumbnail_full_uri")
    private String thumbnailFullUri;

    @Column(name = "thumbnail_path")
    private String thumbnailPath;

    @Column(name = "rental_regions")
    private String rentalRegions;

    @Column(name = "return_regions")
    private String returnRegions;

    @Column(name = "price")
    private Integer price;

    @Column(name = "discount_rate")
    private Integer discountRate;

    @Column(name = "category_id")
    @Type(type = "uuid-char")
    private UUID categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public static ItemEntity toEntity(ItemDto dto) {
        ItemEntity entity = ItemEntity.builder()
                .cid(dto.getCid())
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .thumbnailOriginName(dto.getThumbnailOriginName())
                .thumbnailName(dto.getThumbnailName())
                .thumbnailFullUri(dto.getThumbnailFullUri())
                .thumbnailPath(dto.getThumbnailPath())
                .rentalRegions(dto.getRentalRegions())
                .returnRegions(dto.getReturnRegions())
                .price(dto.getPrice())
                .discountRate(dto.getDiscountRate())
                .categoryId(dto.getCategoryId())
                .categoryName(dto.getCategoryName())
                .createdAt(dto.getCreatedAt())
                .build();
        return entity;
    }
}
