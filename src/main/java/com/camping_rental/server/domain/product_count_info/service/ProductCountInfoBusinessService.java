package com.camping_rental.server.domain.product_count_info.service;

import com.camping_rental.server.domain.product_count_info.entity.ProductCountInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCountInfoBusinessService {
    private final ProductCountInfoService productCountInfoService;

    @Transactional
    public void increseViewCount(UUID productId) {
        ProductCountInfoEntity productCountInfoEntity = productCountInfoService.searchByProductIdOrNull(productId);

        if (productCountInfoEntity != null) {
            Integer viewCount = productCountInfoEntity.getViewCount();
            viewCount++;

            productCountInfoEntity.setViewCount(viewCount);
        } else {
            productCountInfoEntity = ProductCountInfoEntity.builder()
                    .cid(null)
                    .viewCount(1)
                    .productId(productId)
                    .build();
        }


        productCountInfoService.saveAndModify(productCountInfoEntity);
    }
}
