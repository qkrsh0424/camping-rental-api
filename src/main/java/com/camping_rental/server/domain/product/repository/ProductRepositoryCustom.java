package com.camping_rental.server.domain.product.repository;

import com.camping_rental.server.domain.product.projection.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepositoryCustom {
    Optional<ProductProjection.RelatedRoom> qSelectByIdRelatedRoom(UUID id);
    Optional<ProductProjection.RelatedRoomAndRegions> qSelectByIdRelatedRoomAndRegion(UUID id);
    Optional<ProductProjection.RelatedProductCategoryAndRoomAndRegionsAndProductImages> qSelectByIdRelatedProductCategoryAndRoomAndRegionsAndProductImages(UUID productId, Map<String, Object> params);
    Page<ProductProjection.RelatedRoom> qSelectPageRelatedRoom(Map<String, Object> params, Pageable pageable);
    Page<ProductProjection.RelatedRoomAndRegions> qSelectPageRelatedRoomAndRegions(Map<String, Object> params, Pageable pageable);
    List<ProductProjection.RelatedRoomAndRegions> qSelectListByIdsRelatedRoomAndRegions(List<UUID> productIds);
}
