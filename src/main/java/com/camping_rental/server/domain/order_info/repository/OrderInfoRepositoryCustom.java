package com.camping_rental.server.domain.order_info.repository;

import com.camping_rental.server.domain.order_info.entity.OrderInfoEntity;
import com.camping_rental.server.domain.order_info.projection.OrderInfoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderInfoRepositoryCustom {
    List<OrderInfoEntity> qSelectList();

    Page<OrderInfoProjection.JoinOrderItems> qSelectJoinOrderItemsPage(Pageable pageable);
}
