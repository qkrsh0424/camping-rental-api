package com.camping_rental.server.domain.product.service;

import com.camping_rental.server.domain.enums.DeletedFlagEnums;
import com.camping_rental.server.domain.exception.dto.AccessDeniedPermissionException;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.product.dto.ProductDto;
import com.camping_rental.server.domain.product.entity.ProductEntity;
import com.camping_rental.server.domain.product.enums.ProductDeletedFlagEnum;
import com.camping_rental.server.domain.product.enums.ProductDisplayYnEnum;
import com.camping_rental.server.domain.product.enums.ProductPackageYnEnum;
import com.camping_rental.server.domain.product.projection.ProductProjection;
import com.camping_rental.server.domain.product.strategy.ProductSearchStrategy;
import com.camping_rental.server.domain.product.strategy.ProductSearchStrategyContext;
import com.camping_rental.server.domain.product.strategy.ProductSearchStrategyName;
import com.camping_rental.server.domain.product.vo.ProductVo;
import com.camping_rental.server.domain.product_image.dto.ProductImageDto;
import com.camping_rental.server.domain.product_image.entity.ProductImageEntity;
import com.camping_rental.server.domain.product_image.enums.ProductImageDeletedFlagEnum;
import com.camping_rental.server.domain.product_image.service.ProductImageService;
import com.camping_rental.server.domain.product_package.dto.ProductPackageDto;
import com.camping_rental.server.domain.product_package.entity.ProductPackageEntity;
import com.camping_rental.server.domain.product_package.service.ProductPackageService;
import com.camping_rental.server.domain.room.entity.RoomEntity;
import com.camping_rental.server.domain.room.service.RoomService;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.utils.CustomDateUtils;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductBusinessService {
    private final UserService userService;
    private final RoomService roomService;
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final ProductSearchStrategyContext productSearchStrategyContext;
    private final ProductPackageService productPackageService;

    @Transactional(readOnly = true)
    public Object searchOne(
            UUID productId,
            Map<String, Object> params
    ) {
        String related = params.get("related") == null ? "room" : params.get("related").toString();

        productSearchStrategyContext.setStrategy(ProductSearchStrategyName.fromString(related));
        ProductSearchStrategy productSearchStrategy = productSearchStrategyContext.returnStrategy();

        return productSearchStrategy.searchById(productId, params);
    }

    @Transactional(readOnly = true)
    public Object searchOneForModify(UUID productId, UUID roomId) {
        UUID userId = userService.getUserIdOrThrow();
        RoomEntity roomEntity = roomService.searchByIdOrThrow(roomId);

        if (!roomEntity.getUserId().equals(userId)) {
            throw new NotMatchedFormatException("접근 권한이 없습니다.");
        }

        ProductEntity productEntity = productService.searchOneByIdElseThrow(productId);
        if(!productEntity.getRoomId().equals(roomEntity.getId())){
            throw new NotMatchedFormatException("접근 권한이 없습니다.");
        }

        ProductVo.Basic productVo = ProductVo.Basic.toVo(productEntity);
        return productVo;
    }

    @Transactional(readOnly = true)
    public Object searchPage(
            Map<String, Object> params,
            Pageable pageable
    ) {
        String related = params.get("related") == null ? "roomAndRegions" : params.get("related").toString();

        productSearchStrategyContext.setStrategy(ProductSearchStrategyName.fromString(related));
        ProductSearchStrategy productSearchStrategy = productSearchStrategyContext.returnStrategy();

        return productSearchStrategy.searchPage(params, pageable);

    }

    @Transactional(readOnly = true)
    public Object searchListByIds(List<UUID> productIds, Map<String, Object> params) {
        String related = params.get("related") == null ? "roomAndRegions" : params.get("related").toString();

        productSearchStrategyContext.setStrategy(ProductSearchStrategyName.fromString(related));
        ProductSearchStrategy productSearchStrategy = productSearchStrategyContext.returnStrategy();

        return productSearchStrategy.searchListByIds(productIds);
    }

    @Transactional
    public void createOne(UUID roomId, ProductDto.Create productDto) {
        UUID userId = userService.getUserIdOrThrow();

        RoomEntity roomEntity = roomService.searchByIdOrThrow(roomId);

        if (!userId.equals(roomEntity.getUserId())) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        ProductDto.Create.checkFormValid(productDto);

        List<ProductImageDto.Create> productImageDtos = productDto.getProductImages();

        ProductImageDto.Create thumbnailImageDto = productImageDtos.get(0);

        UUID productId = UUID.randomUUID();
        ProductEntity productEntity = ProductEntity.builder()
                .cid(null)
                .id(productId)
                .name(productDto.getName())
                .description(productDto.getDescription())
                .thumbnailUri(thumbnailImageDto.getFileFullUri())
                .price(productDto.getPrice())
                .minimumRentalHour(productDto.getMinimumRentalHour())
                .discountYn(productDto.getDiscountYn())
                .discountMinimumHour(productDto.getDiscountMinimumHour())
                .discountRate(productDto.getDiscountRate())
                .displayYn(ProductDisplayYnEnum.Y.getValue())
                .packageYn(productDto.getPackageYn())
                .maxOrderUnit(productDto.getMaxOrderUnit())
                .deletedFlag(ProductDeletedFlagEnum.EXIST.getValue())
                .createdAt(CustomDateUtils.getCurrentDateTime())
                .updatedAt(CustomDateUtils.getCurrentDateTime())
                .productCategoryId(productDto.getProductCategoryId())
                .roomId(roomEntity.getId())
                .build();

        List<ProductImageEntity> productImageEntities = productImageDtos.stream().map(dto -> {
            ProductImageEntity entity = ProductImageEntity.builder()
                    .cid(null)
                    .id(UUID.randomUUID())
                    .fileName(dto.getFileName())
                    .fileOriginName(dto.getFileOriginName())
                    .fileStorageUri(dto.getFileStorageUri())
                    .fileFullUri(dto.getFileFullUri())
                    .serviceUrl(dto.getServiceUrl())
                    .filePath(dto.getFilePath())
                    .fileExtension(dto.getFileExtension())
                    .madeAt(dto.getMadeAt())
                    .size(dto.getSize())
                    .deletedFlag(ProductImageDeletedFlagEnum.EXIST.getValue())
                    .createdAt(CustomDateUtils.getCurrentDateTime())
                    .productId(productId)
                    .build();
            return entity;
        }).collect(Collectors.toList());

        productService.saveAndModify(productEntity);
        productImageService.saveAll(productImageEntities);

        if (productDto.getPackageYn() != null && productDto.getPackageYn().equals(ProductPackageYnEnum.Y.getValue())) {
            List<ProductPackageDto.Create> productPackageDtos = productDto.getProductPackages();
            if (productPackageDtos.size() <= 0) {
                throw new NotMatchedFormatException("패키지 카테고리는 패키지 구성 제품을 설정해야 합니다.");
            }

            List<ProductPackageEntity> productPackageEntities = productPackageDtos.stream().map(dto -> {
                ProductPackageEntity entity = ProductPackageEntity.builder()
                        .cid(null)
                        .id(UUID.randomUUID())
                        .name(dto.getName())
                        .unit(dto.getUnit())
                        .thumbnailUri(dto.getThumbnailUri())
                        .updatedAt(CustomDateUtils.getCurrentDateTime())
                        .deletedFlag(DeletedFlagEnums.EXIST.getValue())
                        .productId(productId)
                        .build();
                return entity;
            }).collect(Collectors.toList());

            productPackageService.saveAll(productPackageEntities);
        }

    }

    /*
    Get a productProjection
    Get a userId
    Load the productEntity from productProjection
    Load the roomEntity from productProjection
    Compare the userId to roomEntity.getUserId(). If not equal to both, then throw out the access-denied exception
    Dirty check update
     */
    @Transactional
    public void changeDisplayYn(UUID productId, String displayYn) {
        ProductProjection.RelatedRoomAndRegions productProjection = productService.qSearchOneByIdJoinRoomAndRegionOrThrow(productId);
        UUID userId = userService.getUserIdOrThrow();
        ProductEntity productEntity = productProjection.getProductEntity();
        RoomEntity roomEntity = productProjection.getRoomEntity();

        /*
        Compare the userId to roomEntity.getUserId(). If not equal to both, then throw out the access-denied exception
         */
        if (!userId.equals(roomEntity.getUserId())) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        /*
        Dirty check update
         */
        productEntity.setDisplayYn(displayYn);
    }

    @Transactional
    public void deleteOne(UUID productId) {
        ProductProjection.RelatedRoomAndRegions productProjection = productService.qSearchOneByIdJoinRoomAndRegionOrThrow(productId);
        UUID userId = userService.getUserIdOrThrow();
        ProductEntity productEntity = productProjection.getProductEntity();
        RoomEntity roomEntity = productProjection.getRoomEntity();

        /*
        Compare the userId to roomEntity.getUserId(). If not equal to both, then throw out the access-denied exception
         */
        if (!userId.equals(roomEntity.getUserId())) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        productImageService.logicalDeleteByProductId(productEntity.getId());
        productService.logicalDelete(productEntity);
    }

    @Transactional
    public void updateOne(UUID productId, ProductDto.Update productDto) {
        UUID userId = userService.getUserIdOrThrow();
        ProductProjection.RelatedRoom productProjection = productService.qSearchByIdRelatedRoomElseThrow(productId);
        ProductEntity productEntity = productProjection.getProductEntity();
        RoomEntity roomEntity = productProjection.getRoomEntity();

        if (!userId.equals(roomEntity.getUserId())) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        ProductImageDto.Update thumbnailDto = productDto.getProductImages().stream().findFirst().orElseThrow(() -> new NotMatchedFormatException("이미지는 최소 1개 이상 등록해야 합니다."));
        String thumbnailUri = thumbnailDto.getFileFullUri();

        productImageService.logicalDeleteByProductId(productEntity.getId());
        productPackageService.logicalDeleteByProductId(productEntity.getId());

        List<ProductImageEntity> productImageEntities = productDto.getProductImages().stream().map(dto -> {
            ProductImageEntity entity = ProductImageEntity.builder()
                    .cid(null)
                    .id(UUID.randomUUID())
                    .fileName(dto.getFileName())
                    .fileOriginName(dto.getFileOriginName())
                    .fileStorageUri(dto.getFileStorageUri())
                    .fileFullUri(dto.getFileFullUri())
                    .serviceUrl(dto.getServiceUrl())
                    .filePath(dto.getFilePath())
                    .fileExtension(dto.getFileExtension())
                    .madeAt(dto.getMadeAt())
                    .size(dto.getSize())
                    .deletedFlag(ProductImageDeletedFlagEnum.EXIST.getValue())
                    .createdAt(CustomDateUtils.getCurrentDateTime())
                    .productId(productEntity.getId())
                    .build();
            return entity;
        }).collect(Collectors.toList());

        productImageService.saveAll(productImageEntities);

        if (productDto.getPackageYn() != null && productDto.getPackageYn().equals(ProductPackageYnEnum.Y.getValue())) {
            List<ProductPackageDto.Create> productPackageDtos = productDto.getProductPackages();
            if (productPackageDtos.size() <= 0) {
                throw new NotMatchedFormatException("패키지 카테고리는 패키지 구성 제품을 설정해야 합니다.");
            }
            List<ProductPackageEntity> productPackageEntities = productDto.getProductPackages().stream().map(dto -> {
                ProductPackageEntity entity = ProductPackageEntity.builder()
                        .cid(null)
                        .id(UUID.randomUUID())
                        .name(dto.getName())
                        .unit(dto.getUnit())
                        .thumbnailUri(dto.getThumbnailUri())
                        .updatedAt(CustomDateUtils.getCurrentDateTime())
                        .deletedFlag(DeletedFlagEnums.EXIST.getValue())
                        .productId(productEntity.getId())
                        .build();
                return entity;
            }).collect(Collectors.toList());

            productPackageService.saveAll(productPackageEntities);
        }


        productEntity.setName(productDto.getName());
        productEntity.setDescription(productDto.getDescription());
        productEntity.setThumbnailUri(thumbnailUri);
        productEntity.setPrice(productDto.getPrice());
        productEntity.setMinimumRentalHour(productDto.getMinimumRentalHour());
        productEntity.setDiscountYn(productDto.getDiscountYn());
        productEntity.setDiscountMinimumHour(productDto.getDiscountMinimumHour());
        productEntity.setDiscountRate(productDto.getDiscountRate());
        productEntity.setDisplayYn(productDto.getDisplayYn());
        productEntity.setPackageYn(productDto.getPackageYn());
        productEntity.setMaxOrderUnit(productDto.getMaxOrderUnit());
        productEntity.setUpdatedAt(CustomDateUtils.getCurrentDateTime());
        productEntity.setProductCategoryId(productDto.getProductCategoryId());
    }
}
