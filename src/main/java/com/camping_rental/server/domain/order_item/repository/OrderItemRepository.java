package com.camping_rental.server.domain.order_item.repository;

import com.camping_rental.server.domain.order_item.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Integer>, OrderItemRepositoryCustom {
    @Query("SELECT oitem FROM OrderItemEntity oitem WHERE oitem.orderInfoId IN :orderInfoIds")
    List<OrderItemEntity> findByOrderInfoIds(List<UUID> orderInfoIds);
}
