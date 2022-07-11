package com.camping_rental.server.domain.rental_order_product.service;

import com.camping_rental.server.domain.rental_order_product.entity.RentalOrderProductEntity;
import com.camping_rental.server.domain.rental_order_product.projection.RentalOrderProductProjection;
import com.camping_rental.server.domain.rental_order_product.repository.RentalOrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RentalOrderProductService {
    private final RentalOrderProductRepository rentalOrderProductRepository;

    public void saveAll(List<RentalOrderProductEntity> entities){
        rentalOrderProductRepository.saveAll(entities);
    }

    public Page<RentalOrderProductProjection.JoinRentalOrderInfo> qSearchPageJoinRentalOrderInfo(Map<String, Object> params, Pageable pageable) {
        return rentalOrderProductRepository.qSelectPageJoinRentalOrderInfo(params, pageable);
    }

    public List<RentalOrderProductProjection.JoinRentalOrderInfo> qSearchListByIdsJoinRentalOrderInfo(List<UUID> ids) {
        return rentalOrderProductRepository.qSelectListByIdsJoinRentalOrderInfo(ids);
    }
}
