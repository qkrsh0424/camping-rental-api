package com.camping_rental.server.domain.product.strategy;

import com.camping_rental.server.annotation.TrackExecutionTime;
import com.camping_rental.server.domain.product.entity.ProductEntity;
import com.camping_rental.server.domain.product.projection.ProductProjection;
import com.camping_rental.server.domain.product.service.ProductService;
import com.camping_rental.server.domain.product.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductSearchStrategyImpl {
    @Component
    @RequiredArgsConstructor
    public static class Basic implements ProductSearchStrategy {
        public final ProductService productService;

        @Override
        public <T> T searchById(UUID id) {
            ProductEntity productEntity = productService.searchOneById(id);
            ProductVo.Basic productVo = ProductVo.Basic.toVo(productEntity);
            return (T) productVo;
        }

        @Override
        public ProductSearchStrategyName getStrategyName() {
            return ProductSearchStrategyName.Basic;
        }
    }

    @Component
    @RequiredArgsConstructor
    public static class RelatedRoom implements ProductSearchStrategy {
        public final ProductService productService;

        @Override
        public <T> Page<T> searchPage(Map<String, Object> params, Pageable pageable) {
            Page<ProductProjection.RelatedRoom> productProjectionPage = productService.qSearchPageRelatedRoom(params, pageable);
            List<ProductProjection.RelatedRoom> productProjections = productProjectionPage.getContent();

            List<ProductVo.RelatedRoom> productVos = productProjections.stream().map(ProductVo.RelatedRoom::toVo).collect(Collectors.toList());
            return (Page<T>) new PageImpl<>(productVos, productProjectionPage.getPageable(), productProjectionPage.getTotalElements());
        }


        @Override
        public ProductSearchStrategyName getStrategyName() {
            return ProductSearchStrategyName.Room;
        }
    }

    @Component
    @RequiredArgsConstructor
    public static class RelatedRoomAndRegions implements ProductSearchStrategy {
        public final ProductService productService;

        @Override
        public <T> T searchById(UUID id) {
            ProductProjection.RelatedRoomAndRegions productProjection = productService.qSearchOneByIdJoinRoomAndRegionOrThrow(id);
            ProductVo.RelatedRoomAndRegions vo = ProductVo.RelatedRoomAndRegions.toVo(productProjection);
            return (T) vo;
        }

        @Override
        public <T> Page<T> searchPage(Map<String, Object> params, Pageable pageable) {
            Page<ProductProjection.RelatedRoomAndRegions> productProjectionPage = productService.qSearchPageRelatedRoomAndRegions(params, pageable);
            List<ProductProjection.RelatedRoomAndRegions> productProjections = productProjectionPage.getContent();

            List<ProductVo.RelatedRoomAndRegions> productVos = productProjections.stream().map(ProductVo.RelatedRoomAndRegions::toVo).collect(Collectors.toList());
            return (Page<T>) new PageImpl<>(productVos, productProjectionPage.getPageable(), productProjectionPage.getTotalElements());
        }

        @Override
        public <T> List<T> searchListByIds(List<UUID> ids) {
            List<ProductProjection.RelatedRoomAndRegions> productProjections = productService.qSearchListByIdsRelatedRoomAndRegions(ids);
            List<ProductVo.RelatedRoomAndRegions> productVos = productProjections.stream().map(ProductVo.RelatedRoomAndRegions::toVo).collect(Collectors.toList());

            return (List<T>) productVos;
        }

        @Override
        public ProductSearchStrategyName getStrategyName() {
            return ProductSearchStrategyName.RoomAndRegions;
        }
    }

    @Component
    @RequiredArgsConstructor
    public static class RelatedProductCategoryAndRoomAndRegionsAndImages implements ProductSearchStrategy {
        public final ProductService productService;

        @Override
        public <T> T searchById(UUID id, Map<String, Object> params) {
            ProductProjection.RelatedProductCategoryAndRoomAndRegionsAndProductImages productProjection = productService.qSearchByIdRelatedCategoryAndRoomAndRegionsAndImagesElseThrow(id, params);

            return (T) ProductVo.RelatedProductCategoryAndRoomAndRegionsAndProductImages.toVo(productProjection);
        }

        @Override
        public ProductSearchStrategyName getStrategyName() {
            return ProductSearchStrategyName.CategoryAndRoomAndRegionsAndImages;
        }
    }
}
