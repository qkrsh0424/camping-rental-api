package com.camping_rental.server.domain.rental_order_product.repository;

import com.camping_rental.server.domain.rental_order_product.projection.RentalOrderProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface RentalOrderProductRepositoryCustom {
    Page<RentalOrderProductProjection.JoinRentalOrderInfo> qSelectPageJoinRentalOrderInfo(Map<String, Object> params, Pageable pageable);
    List<RentalOrderProductProjection.JoinRentalOrderInfo> qSelectListByIdsJoinRentalOrderInfo(List<UUID> ids);
}
