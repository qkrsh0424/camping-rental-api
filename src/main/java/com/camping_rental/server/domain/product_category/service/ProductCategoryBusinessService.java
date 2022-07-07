package com.camping_rental.server.domain.product_category.service;

import com.camping_rental.server.domain.product_category.entity.ProductCategoryEntity;
import com.camping_rental.server.domain.product_category.vo.ProductCategoryVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductCategoryBusinessService {
    private final ProductCategoryService productCategoryService;

    public Object searchList(){
        List<ProductCategoryEntity> productCategoryEntities = productCategoryService.searchList();
        List<ProductCategoryVo.Basic> productCategoryVos = productCategoryEntities.stream().map(entity ->{
            return ProductCategoryVo.Basic.toVo(entity);
        }).collect(Collectors.toList());

        return productCategoryVos;
    }

    public Object searchListByRoom(UUID roomId) {
        List<ProductCategoryEntity> productCategoryEntities = productCategoryService.qSearchListByRoom(roomId);
        List<ProductCategoryVo.Basic> productCategoryVos = productCategoryEntities.stream().map(entity ->{
            return ProductCategoryVo.Basic.toVo(entity);
        }).collect(Collectors.toList());

        return productCategoryVos;
    }
}
