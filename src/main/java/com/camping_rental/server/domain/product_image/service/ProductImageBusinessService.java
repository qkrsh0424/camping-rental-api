package com.camping_rental.server.domain.product_image.service;

import com.camping_rental.server.domain.product_image.entity.ProductImageEntity;
import com.camping_rental.server.domain.product_image.vo.ProductImageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductImageBusinessService {
    private final ProductImageService productImageService;

    public Object searchListByProductId(UUID productId){
        List<ProductImageEntity> productImageEntities = productImageService.searchListByProductId(productId);
        List<ProductImageVo.Basic> productImageVos = productImageEntities.stream().map(r->{
            ProductImageVo.Basic vo = ProductImageVo.Basic.toVo(r);
            return vo;
        }).collect(Collectors.toList());

        return productImageVos;
    }
}
