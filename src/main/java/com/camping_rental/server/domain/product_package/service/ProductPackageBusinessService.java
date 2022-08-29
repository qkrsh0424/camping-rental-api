package com.camping_rental.server.domain.product_package.service;

import com.camping_rental.server.domain.product_package.entity.ProductPackageEntity;
import com.camping_rental.server.domain.product_package.vo.ProductPackageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductPackageBusinessService {
    private final ProductPackageService productPackageService;

    public Object searchListByProductId(UUID productId){
        List<ProductPackageEntity> productPackageEntities = productPackageService.searchListByProductId(productId);

        List<ProductPackageVo.Basic> productPackageVos = productPackageEntities.stream().map(entity->{
            ProductPackageVo.Basic vo = ProductPackageVo.Basic.toVo(entity);
            return vo;
        }).collect(Collectors.toList());

        return productPackageVos;
    }
}
