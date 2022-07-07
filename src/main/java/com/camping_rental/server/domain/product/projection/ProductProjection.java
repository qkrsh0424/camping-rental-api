package com.camping_rental.server.domain.product.projection;

import com.camping_rental.server.domain.product.entity.ProductEntity;
import com.camping_rental.server.domain.product_category.entity.ProductCategoryEntity;
import com.camping_rental.server.domain.product_image.entity.ProductImageEntity;
import com.camping_rental.server.domain.region.entity.RegionEntity;
import com.camping_rental.server.domain.room.entity.RoomEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductProjection {
    private ProductEntity productEntity;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class JoinRoomAndRegions{
        private ProductEntity productEntity;
        private RoomEntity roomEntity;
        private List<RegionEntity> regionEntities;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FullJoin {
        private ProductEntity productEntity;
        private ProductCategoryEntity productCategoryEntity;
        private RoomEntity roomEntity;
        private Set<ProductImageEntity> productImageEntities;
        private Set<RegionEntity> regionEntities;
    }
}
