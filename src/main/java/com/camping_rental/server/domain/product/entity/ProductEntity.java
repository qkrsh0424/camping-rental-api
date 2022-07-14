package com.camping_rental.server.domain.product.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product")
@Entity
@DynamicUpdate
@DynamicInsert
@Where(clause = "deleted_flag=0")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Long cid;

    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "thumbnail_uri")
    private String thumbnailUri;

    @Column(name = "price")
    private Integer price;

    @Column(name = "minimum_rental_hour")
    private Integer minimumRentalHour;

    @Column(name = "discount_yn")
    private String discountYn;

    @Column(name = "discount_minimum_hour")
    private Integer discountMinimumHour;

    @Column(name = "discount_rate")
    private Integer discountRate;

    @Column(name = "displayYn")
    private String displayYn;

    @Column(name = "deletedFlag")
    private boolean deletedFlag;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "product_category_id")
    @Type(type = "uuid-char")
    private UUID productCategoryId;

    @Column(name = "room_id")
    @Type(type = "uuid-char")
    private UUID roomId;
}
