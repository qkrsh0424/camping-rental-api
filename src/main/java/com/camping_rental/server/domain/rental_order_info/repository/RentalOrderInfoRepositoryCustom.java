package com.camping_rental.server.domain.rental_order_info.repository;

import com.camping_rental.server.domain.rental_order_info.projection.RentalOrderInfoProjection;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentalOrderInfoRepositoryCustom {
    Optional<RentalOrderInfoProjection.FullJoin> qSelectOneFullJoinByOrderNumberAndOrdererAndOrdererPhoneNumber(String orderNumber, String orderer, String ordererPhoneNumber);
}
