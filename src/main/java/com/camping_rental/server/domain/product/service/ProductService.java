package com.camping_rental.server.domain.product.service;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.product.entity.ProductEntity;
import com.camping_rental.server.domain.product.enums.ProductDeletedFlagEnum;
import com.camping_rental.server.domain.product.projection.ProductProjection;
import com.camping_rental.server.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public void saveAndModify(ProductEntity entity){
        productRepository.save(entity);
    }

    public ProductProjection.RelatedProductCategoryAndRoomAndRegionsAndProductImages qSearchByIdRelatedCategoryAndRoomAndRegionsAndImagesElseThrow(UUID productId, Map<String, Object> params) {
        return productRepository.qSelectByIdRelatedProductCategoryAndRoomAndRegionsAndProductImages(productId, params).orElseThrow(()->new NotMatchedFormatException("요청 데이터를 찾을 수 없습니다."));
    }

    public Page<ProductProjection.RelatedRoom> qSearchPageRelatedRoom(Map<String, Object> params, Pageable pageable) {
        return productRepository.qSelectPageRelatedRoom(params, pageable);
    }

    public Page<ProductProjection.RelatedRoomAndRegions> qSearchPageRelatedRoomAndRegions(Map<String, Object> params, Pageable pageable) {
        return productRepository.qSelectPageRelatedRoomAndRegions(params, pageable);
    }

    public ProductProjection.RelatedRoomAndRegions qSearchOneByIdJoinRoomAndRegionOrThrow(UUID productId) {
        return productRepository.qSelectByIdRelatedRoomAndRegion(productId).orElseThrow(() -> new NotMatchedFormatException("요청 데이터를 찾을 수 없습니다."));
    }

    public List<ProductProjection.RelatedRoomAndRegions> qSearchListByIdsRelatedRoomAndRegions(List<UUID> productIds) {
        return productRepository.qSelectListByIdsRelatedRoomAndRegions(productIds);
    }

    public void logicalDelete(ProductEntity entity) {
        entity.setDeletedFlag(ProductDeletedFlagEnum.DELETED.getValue());
    }

    public ProductEntity searchOneByIdElseThrow(UUID productId) {
        return productRepository.findById(productId).orElseThrow(() -> new NotMatchedFormatException("요청 데이터를 찾을 수 없습니다."));
    }

    public ProductProjection.RelatedRoom qSearchByIdRelatedRoomElseThrow(UUID productId) {
        return productRepository.qSelectByIdRelatedRoom(productId).orElseThrow(()->new NotMatchedFormatException("요청 데이터를 찾을 수 없습니다."));
    }
}
