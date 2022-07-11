package com.camping_rental.server.domain.product_image.entity;

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
@Table(name = "product_image")
@Entity
@DynamicUpdate
@DynamicInsert
@Where(clause = "deleted_flag=0")
public class ProductImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Long cid;

    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_origin_name")
    private String fileOriginName;

    @Column(name = "file_storage_uri")
    private String fileStorageUri;

    @Column(name = "file_full_uri")
    private String fileFullUri;

    @Column(name = "service_url")
    private String serviceUrl;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_extension")
    private String fileExtension;

    @Column(name = "made_at")
    private LocalDateTime madeAt;

    @Column(name = "size")
    private Long size;

    @Column(name = "deleted_flag")
    private boolean deletedFlag;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "product_id")
    @Type(type = "uuid-char")
    private UUID productId;
}
