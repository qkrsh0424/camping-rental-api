package com.camping_rental.server.domain.item.dto;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    private Integer cid;
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
    private UUID categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
}
