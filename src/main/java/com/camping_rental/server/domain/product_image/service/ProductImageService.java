package com.camping_rental.server.domain.product_image.service;

import com.camping_rental.server.domain.product_image.entity.ProductImageEntity;
import com.camping_rental.server.domain.product_image.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final ProductImageRepository productImageRepository;

    public void saveAndModify(ProductImageEntity entity){
        productImageRepository.save(entity);
    }

    public void saveAll(List<ProductImageEntity> entities){
        productImageRepository.saveAll(entities);
    }

    public void logicalDeleteByProductId(UUID productId) {
        productImageRepository.logicalDeleteByProductId(productId);
    }
}
