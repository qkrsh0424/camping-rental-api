package com.camping_rental.server.domain.product.strategy;

import com.camping_rental.server.domain.product.projection.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ProductSearchPageStrategy {
    Page<ProductProjection.JoinRoomAndRegions> search(Map<String, Object> params, Pageable pageable);
    ProductSearchPageStrategyName getStrategyName();
}
