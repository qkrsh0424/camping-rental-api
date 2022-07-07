package com.camping_rental.server.domain.rental_order_product.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "rental_order_product")
@DynamicInsert
@DynamicUpdate
@Where(clause = "deleted_flag=0")
public class RentalOrderProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Long cid;

    @Column(name = "id")
    @Type(type="uuid-char")
    private UUID id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "thumbnail_uri")
    private String thumbnailUri;

    @Column(name = "price")
    private Integer price;

    @Column(name = "discount_rate")
    private Integer discountRate;

    @Column(name = "unit")
    private Integer unit;

    @Column(name = "deleted_flag")
    private boolean deletedFlag;

    @Column(name = "rental_order_info_id")
    @Type(type="uuid-char")
    private UUID rentalOrderInfoId;

    @Column(name = "product_id")
    @Type(type="uuid-char")
    private UUID productId;
}
