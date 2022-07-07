package com.camping_rental.server.domain.product_category.repository;

import com.camping_rental.server.domain.product_category.entity.ProductCategoryEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ProductCategoryRepositoryCustom{
    List<ProductCategoryEntity> qSelectListByRoomId(UUID roomId);
}
