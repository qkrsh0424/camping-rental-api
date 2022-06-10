package com.camping_rental.server.domain.order_info.repository;

import com.camping_rental.server.domain.order_info.entity.OrderInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfoEntity, Integer> {
    Optional<OrderInfoEntity> findById(UUID id);
}
