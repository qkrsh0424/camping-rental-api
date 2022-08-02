package com.camping_rental.server.domain.product.strategy;

import com.camping_rental.server.domain.product.projection.ProductProjection;
import com.camping_rental.server.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Map;

public class ProductSearchPage {

    @Component
    @RequiredArgsConstructor
    public static class OrderByRank implements ProductSearchPageStrategy {
        public final ProductService productService;

        @Override
        public Page<ProductProjection.JoinRoomAndRegions> search(Map<String, Object> params, Pageable pageable) {
            return productService.qSearchPageJoinRoomAndRegionsOrderByRank(params, pageable);
        }

        @Override
        public ProductSearchPageStrategyName getStrategyName() {
            return ProductSearchPageStrategyName.OrderByRank;
        }
    }

    @Component
    @RequiredArgsConstructor
    public static class OrderByNew implements ProductSearchPageStrategy {
        public final ProductService productService;

        @Override
        public Page<ProductProjection.JoinRoomAndRegions> search(Map<String, Object> params, Pageable pageable) {
            return productService.qSearchPageJoinRoomAndRegionsOrderByNew(params, pageable);
        }

        @Override
        public ProductSearchPageStrategyName getStrategyName() {
            return ProductSearchPageStrategyName.OrderByNew;
        }
    }
}
