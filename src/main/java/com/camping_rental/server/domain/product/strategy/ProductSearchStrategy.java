package com.camping_rental.server.domain.product.strategy;

import com.camping_rental.server.domain.product.projection.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProductSearchStrategy {
    default <T> T searchById(UUID id) {
        return null;
    }
    default <T> T searchById(UUID id, Map<String, Object> params) {
        return null;
    }

    default <T> Page<T> searchPage(Map<String, Object> params, Pageable pageable) {
        return null;
    }

    default <T> List<T> searchListByIds(List<UUID> ids){
        return null;
    }

    default ProductSearchStrategyName getStrategyName() {
        return null;
    }
}
