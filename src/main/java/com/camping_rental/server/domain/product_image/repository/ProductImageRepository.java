package com.camping_rental.server.domain.product_image.repository;

import com.camping_rental.server.domain.product_image.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {
    @Modifying
    @Query("UPDATE ProductImageEntity pi SET pi.deletedFlag=1 WHERE pi.productId=:productId")
    void logicalDeleteByProductId(UUID productId);
}
