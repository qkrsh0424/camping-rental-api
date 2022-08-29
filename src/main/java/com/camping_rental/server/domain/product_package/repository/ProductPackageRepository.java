package com.camping_rental.server.domain.product_package.repository;

import com.camping_rental.server.domain.product_package.entity.ProductPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductPackageRepository extends JpaRepository<ProductPackageEntity, Long> {
    List<ProductPackageEntity> findByProductId(UUID productId);

    List<ProductPackageEntity> findAllByProductId(UUID productId);

    @Modifying
    @Query("UPDATE ProductPackageEntity pp SET pp.deletedFlag=1 WHERE pp.productId=:productId")
    void logicalDeleteByProductId(UUID productId);
}
