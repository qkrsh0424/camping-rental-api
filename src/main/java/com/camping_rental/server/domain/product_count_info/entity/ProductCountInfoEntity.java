package com.camping_rental.server.domain.product_count_info.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product_count_info")
@Entity
@DynamicUpdate
@DynamicInsert
public class ProductCountInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Long cid;
    @Column(name = "view_count")
    private Integer viewCount;
    @Column(name = "product_id")
    @Type(type = "uuid-char")
    private UUID productId;
}
