package com.camping_rental.server.domain.rental_order_info.service;

import com.camping_rental.server.domain.rental_order_info.entity.RentalOrderInfoEntity;
import com.camping_rental.server.domain.rental_order_info.repository.RentalOrderInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalOrderInfoService {
    private final RentalOrderInfoRepository rentalOrderInfoRepository;

    public void saveAndModify(RentalOrderInfoEntity entity){
        rentalOrderInfoRepository.save(entity);
    }
}
