package com.camping_rental.server.domain.product_category.entity;

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
@Table(name = "product_category")
@Entity
@DynamicInsert
@DynamicUpdate
@Where(clause = "deleted_flag=0")
public class ProductCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "deleted_flag")
    private boolean deletedFlag;

    @Column(name = "main_category_cid")
    private Integer mainCategoryCid;

    @Column(name = "main_category_id")
    @Type(type = "uuid-char")
    private UUID mainCategoryId;
}
