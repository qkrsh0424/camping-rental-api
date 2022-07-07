package com.camping_rental.server.domain.rental_order_info.repository;

import com.camping_rental.server.domain.rental_order_info.entity.RentalOrderInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalOrderInfoRepository extends JpaRepository<RentalOrderInfoEntity, Long> {
}
