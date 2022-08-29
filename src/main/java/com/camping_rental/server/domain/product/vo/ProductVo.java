package com.camping_rental.server.domain.product.vo;

import com.camping_rental.server.domain.product.entity.ProductEntity;
import com.camping_rental.server.domain.product.projection.ProductProjection;
import com.camping_rental.server.domain.product_category.vo.ProductCategoryVo;
import com.camping_rental.server.domain.product_image.vo.ProductImageVo;
import com.camping_rental.server.domain.region.vo.RegionVo;
import com.camping_rental.server.domain.room.vo.RoomVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVo {
    private Long cid;
    private UUID id;
    private String name;
    private String description;
    private String thumbnailUri;
    private Integer price;
    private Integer minimumRentalHour;
    private String discountYn;
    private Integer discountMinimumHour;
    private Integer discountRate;
    private String displayYn;
    private String packageYn;
    private Integer maxOrderUnit;
    private boolean deletedFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID productCategoryId;
    private UUID roomId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Basic {
        private UUID id;
        private String name;
        private String description;
        private String thumbnailUri;
        private Integer price;
        private Integer minimumRentalHour;
        private String discountYn;
        private Integer discountMinimumHour;
        private Integer discountRate;
        private String displayYn;
        private String packageYn;
        private Integer maxOrderUnit;
        private UUID productCategoryId;
        private UUID roomId;

        public static Basic toVo(ProductEntity entity) {
            Basic vo = Basic.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .description(entity.getDescription())
                    .thumbnailUri(entity.getThumbnailUri())
                    .price(entity.getPrice())
                    .minimumRentalHour(entity.getMinimumRentalHour())
                    .discountYn(entity.getDiscountYn())
                    .discountMinimumHour(entity.getDiscountMinimumHour())
                    .discountRate(entity.getDiscountRate())
                    .displayYn(entity.getDisplayYn())
                    .packageYn(entity.getPackageYn())
                    .maxOrderUnit(entity.getMaxOrderUnit())
                    .productCategoryId(entity.getProductCategoryId())
                    .roomId(entity.getRoomId())
                    .build();
            return vo;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RelatedRoom {
        private UUID id;
        private String name;
        private String description;
        private String thumbnailUri;
        private Integer price;
        private Integer minimumRentalHour;
        private String discountYn;
        private Integer discountMinimumHour;
        private Integer discountRate;
        private String displayYn;
        private String packageYn;
        private Integer maxOrderUnit;
        private UUID productCategoryId;
        private UUID roomId;
        private RoomVo.Basic room;

        public static RelatedRoom toVo(ProductProjection.RelatedRoom proj) {
            RelatedRoom vo = RelatedRoom.builder()
                    .id(proj.getProductEntity().getId())
                    .name(proj.getProductEntity().getName())
                    .description(proj.getProductEntity().getDescription())
                    .thumbnailUri(proj.getProductEntity().getThumbnailUri())
                    .price(proj.getProductEntity().getPrice())
                    .minimumRentalHour(proj.getProductEntity().getMinimumRentalHour())
                    .discountYn(proj.getProductEntity().getDiscountYn())
                    .discountMinimumHour(proj.getProductEntity().getDiscountMinimumHour())
                    .discountRate(proj.getProductEntity().getDiscountRate())
                    .displayYn(proj.getProductEntity().getDisplayYn())
                    .packageYn(proj.getProductEntity().getPackageYn())
                    .maxOrderUnit(proj.getProductEntity().getMaxOrderUnit())
                    .productCategoryId(proj.getProductEntity().getProductCategoryId())
                    .roomId(proj.getProductEntity().getRoomId())
                    .room(RoomVo.Basic.toVo(proj.getRoomEntity()))
                    .build();
            return vo;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RelatedRoomAndRegions {
        private UUID id;
        private String name;
        private String description;
        private String thumbnailUri;
        private Integer price;
        private Integer minimumRentalHour;
        private String discountYn;
        private Integer discountMinimumHour;
        private Integer discountRate;
        private String displayYn;
        private String packageYn;
        private Integer maxOrderUnit;
        private UUID productCategoryId;
        private UUID roomId;
        private RoomVo.Basic room;
        private List<RegionVo.Basic> regions;

        public static RelatedRoomAndRegions toVo(ProductProjection.RelatedRoomAndRegions proj) {
            RelatedRoomAndRegions vo = RelatedRoomAndRegions.builder()
                    .id(proj.getProductEntity().getId())
                    .name(proj.getProductEntity().getName())
                    .description(proj.getProductEntity().getDescription())
                    .thumbnailUri(proj.getProductEntity().getThumbnailUri())
                    .price(proj.getProductEntity().getPrice())
                    .minimumRentalHour(proj.getProductEntity().getMinimumRentalHour())
                    .discountYn(proj.getProductEntity().getDiscountYn())
                    .discountMinimumHour(proj.getProductEntity().getDiscountMinimumHour())
                    .discountRate(proj.getProductEntity().getDiscountRate())
                    .displayYn(proj.getProductEntity().getDisplayYn())
                    .packageYn(proj.getProductEntity().getPackageYn())
                    .maxOrderUnit(proj.getProductEntity().getMaxOrderUnit())
                    .productCategoryId(proj.getProductEntity().getProductCategoryId())
                    .roomId(proj.getProductEntity().getRoomId())
                    .room(RoomVo.Basic.toVo(proj.getRoomEntity()))
                    .regions(proj.getRegionEntities().stream().map(r -> RegionVo.Basic.toVo(r)).collect(Collectors.toList()))
                    .build();
            return vo;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class JoinRegions {
        private UUID id;
        private String name;
        private String description;
        private String thumbnailUri;
        private Integer price;
        private Integer minimumRentalHour;
        private String discountYn;
        private Integer discountMinimumHour;
        private Integer discountRate;
        private String displayYn;
        private String packageYn;
        private Integer maxOrderUnit;
        private UUID productCategoryId;
        private UUID roomId;
        private List<RegionVo.Basic> regions;

        public static JoinRegions toVo(ProductProjection.RelatedRoomAndRegions proj) {
            JoinRegions vo = JoinRegions.builder()
                    .id(proj.getProductEntity().getId())
                    .name(proj.getProductEntity().getName())
                    .description(proj.getProductEntity().getDescription())
                    .thumbnailUri(proj.getProductEntity().getThumbnailUri())
                    .price(proj.getProductEntity().getPrice())
                    .minimumRentalHour(proj.getProductEntity().getMinimumRentalHour())
                    .discountYn(proj.getProductEntity().getDiscountYn())
                    .discountMinimumHour(proj.getProductEntity().getDiscountMinimumHour())
                    .discountRate(proj.getProductEntity().getDiscountRate())
                    .displayYn(proj.getProductEntity().getDisplayYn())
                    .packageYn(proj.getProductEntity().getPackageYn())
                    .maxOrderUnit(proj.getProductEntity().getMaxOrderUnit())
                    .productCategoryId(proj.getProductEntity().getProductCategoryId())
                    .roomId(proj.getProductEntity().getRoomId())
                    .regions(proj.getRegionEntities().stream().map(r -> RegionVo.Basic.toVo(r)).collect(Collectors.toList()))
                    .build();
            return vo;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RelatedProductCategoryAndRoomAndRegionsAndProductImages {
        private UUID id;
        private String name;
        private String description;
        private String thumbnailUri;
        private Integer price;
        private Integer minimumRentalHour;
        private String discountYn;
        private Integer discountMinimumHour;
        private Integer discountRate;
        private String displayYn;
        private String packageYn;
        private Integer maxOrderUnit;
        private UUID productCategoryId;
        private UUID roomId;
        private RoomVo.Basic room;
        private ProductCategoryVo.Basic productCategory;
        private List<ProductImageVo.Basic> productImages;
        private List<RegionVo.Basic> regions;

        public static RelatedProductCategoryAndRoomAndRegionsAndProductImages toVo(ProductProjection.RelatedProductCategoryAndRoomAndRegionsAndProductImages proj) {
            RelatedProductCategoryAndRoomAndRegionsAndProductImages vo = RelatedProductCategoryAndRoomAndRegionsAndProductImages.builder()
                    .id(proj.getProductEntity().getId())
                    .name(proj.getProductEntity().getName())
                    .description(proj.getProductEntity().getDescription())
                    .thumbnailUri(proj.getProductEntity().getThumbnailUri())
                    .price(proj.getProductEntity().getPrice())
                    .minimumRentalHour(proj.getProductEntity().getMinimumRentalHour())
                    .discountYn(proj.getProductEntity().getDiscountYn())
                    .discountMinimumHour(proj.getProductEntity().getDiscountMinimumHour())
                    .discountRate(proj.getProductEntity().getDiscountRate())
                    .displayYn(proj.getProductEntity().getDisplayYn())
                    .packageYn(proj.getProductEntity().getPackageYn())
                    .maxOrderUnit(proj.getProductEntity().getMaxOrderUnit())
                    .productCategoryId(proj.getProductEntity().getProductCategoryId())
                    .roomId(proj.getProductEntity().getRoomId())
                    .room(RoomVo.Basic.toVo(proj.getRoomEntity()))
                    .productCategory(ProductCategoryVo.Basic.toVo(proj.getProductCategoryEntity()))
                    .productImages(proj.getProductImageEntities().stream().map(r -> ProductImageVo.Basic.toVo(r)).collect(Collectors.toList()))
                    .regions(proj.getRegionEntities().stream().map(r -> RegionVo.Basic.toVo(r)).collect(Collectors.toList()))
                    .build();
            return vo;
        }
    }
}
