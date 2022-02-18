package com.camping_rental.server.domain.order_info.repository;

import com.camping_rental.server.domain.order_info.entity.OrderInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfoEntity, Integer> {
}
