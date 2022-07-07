package com.camping_rental.server.domain.rental_order_product.service;

import com.camping_rental.server.domain.rental_order_product.entity.RentalOrderProductEntity;
import com.camping_rental.server.domain.rental_order_product.repository.RentalOrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalOrderProductService {
    private final RentalOrderProductRepository rentalOrderProductRepository;

    public void saveAll(List<RentalOrderProductEntity> entities){
        rentalOrderProductRepository.saveAll(entities);
    }
}
