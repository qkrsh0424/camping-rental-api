package com.camping_rental.server.domain.product_category.service;

import com.camping_rental.server.domain.product_category.entity.ProductCategoryEntity;
import com.camping_rental.server.domain.product_category.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public List<ProductCategoryEntity> searchList(){
        return productCategoryRepository.findAll();
    }

    public List<ProductCategoryEntity> qSearchListByRoom(UUID roomId) {
        return productCategoryRepository.qSelectListByRoomId(roomId);
    }
}
