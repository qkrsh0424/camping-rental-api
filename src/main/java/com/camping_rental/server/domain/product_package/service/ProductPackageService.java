package com.camping_rental.server.domain.product_package.service;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.product_image.entity.ProductImageEntity;
import com.camping_rental.server.domain.product_package.entity.ProductPackageEntity;
import com.camping_rental.server.domain.product_package.repository.ProductPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductPackageService {
    private final ProductPackageRepository productPackageRepository;

    public List<ProductPackageEntity> searchListByProductId(UUID productId) {
        return productPackageRepository.findAllByProductId(productId);
    }

    public void saveAll(List<ProductPackageEntity> entities){
        productPackageRepository.saveAll(entities);
    }


    public void logicalDeleteByProductId(UUID productId) {
        productPackageRepository.logicalDeleteByProductId(productId);
    }
}
