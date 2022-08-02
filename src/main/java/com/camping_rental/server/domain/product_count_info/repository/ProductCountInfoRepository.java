package com.camping_rental.server.domain.product_count_info.repository;

import com.camping_rental.server.domain.product_count_info.entity.ProductCountInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductCountInfoRepository extends JpaRepository<ProductCountInfoEntity, Long> {
    Optional<ProductCountInfoEntity> findByProductId(UUID productId);
}
