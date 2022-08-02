package com.camping_rental.server.domain.product_count_info.service;

import com.camping_rental.server.domain.product_count_info.entity.ProductCountInfoEntity;
import com.camping_rental.server.domain.product_count_info.repository.ProductCountInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCountInfoService {
    private final ProductCountInfoRepository productCountInfoRepository;

    public ProductCountInfoEntity searchByProductIdOrNull(UUID productId){
        return productCountInfoRepository.findByProductId(productId).orElse(null);
    }

    public void saveAndModify(ProductCountInfoEntity productCountInfoEntity) {
        productCountInfoRepository.save(productCountInfoEntity);
    }
}
