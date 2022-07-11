package com.camping_rental.server.domain.rental_order_product.repository;

import com.camping_rental.server.domain.rental_order_product.entity.RentalOrderProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalOrderProductRepository extends JpaRepository<RentalOrderProductEntity, Long>, RentalOrderProductRepositoryCustom {
}
