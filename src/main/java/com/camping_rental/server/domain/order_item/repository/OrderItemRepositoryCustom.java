package com.camping_rental.server.domain.order_item.repository;

import com.camping_rental.server.domain.order_item.projection.OrderItemProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepositoryCustom {
    Page<OrderItemProjection.M2OJ> qSelectM2OJPage(Pageable pageable);
}
