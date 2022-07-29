package com.camping_rental.server.domain.rental_order_info.repository;

import com.camping_rental.server.domain.rental_order_info.projection.RentalOrderInfoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RentalOrderInfoRepositoryCustom {
    Optional<RentalOrderInfoProjection.FullJoin> qSelectOneFullJoinByOrderNumberAndOrdererAndOrdererPhoneNumber(String orderNumber, String orderer, String ordererPhoneNumber);

    Page<RentalOrderInfoProjection.FullJoin> qSelectPageFullJoinByRoomId(UUID roomId, Pageable pageable);
}
