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
    Optional<ProductProjection.JoinRoomAndRegions> qSelectOneByIdJoinRoomAndRegion(UUID id);

    List<ProductProjection.JoinRoomAndRegions> qSelectListJoinRoomAndRegions(UUID roomId, Map<String, Object> params);
    List<ProductProjection.JoinRoomAndRegions> qSelectListJoinRoomAndRegions();
    Page<ProductProjection.JoinRoomAndRegions> qSelectPageJoinRoomAndRegions(Map<String, Object> params, Pageable pageable);

    Optional<ProductProjection.FullJoin> qSelectOneFullJoin(UUID productId);

    List<ProductProjection.JoinRoomAndRegions> qSelectListByIdsJoinRoomAndRegions(List<UUID> productIds);
}
