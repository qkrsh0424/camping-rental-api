package com.camping_rental.server.domain.product.service;

import com.camping_rental.server.domain.exception.dto.AccessDeniedPermissionException;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.product.dto.ProductDto;
import com.camping_rental.server.domain.product.entity.ProductEntity;
import com.camping_rental.server.domain.product.enums.ProductDeletedFlagEnum;
import com.camping_rental.server.domain.product.enums.ProductDisplayYnEnum;
import com.camping_rental.server.domain.product.projection.ProductProjection;
import com.camping_rental.server.domain.product.strategy.ProductSearchPageFactory;
import com.camping_rental.server.domain.product.strategy.ProductSearchPageStrategy;
import com.camping_rental.server.domain.product.strategy.ProductSearchPageStrategyName;
import com.camping_rental.server.domain.product.vo.ProductVo;
import com.camping_rental.server.domain.product_image.dto.ProductImageDto;
import com.camping_rental.server.domain.product_image.entity.ProductImageEntity;
import com.camping_rental.server.domain.product_image.enums.ProductImageDeletedFlagEnum;
import com.camping_rental.server.domain.product_image.service.ProductImageService;
import com.camping_rental.server.domain.room.entity.RoomEntity;
import com.camping_rental.server.domain.room.service.RoomService;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.utils.CustomDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ProductSearchPageFactory productSearchPageFactory;

    @Transactional
    public Object searchOne(UUID productId) {
        ProductProjection.FullJoin productProjection = productService.qSearchOneFullJoin(productId);
        ProductVo.FullJoin productVo = ProductVo.FullJoin.toVo(productProjection);
        return productVo;
    }

    @Transactional(readOnly = true)
    public Object searchPage(Map<String, Object> params, Pageable pageable) {
        String orderType = null;
        try {
            orderType = params.get("orderType").toString();
        } catch (IllegalArgumentException | NullPointerException e) {
            orderType = "order_rank";
        }

        ProductSearchPageStrategy productSearchPageStrategy = productSearchPageFactory.findStrategy(ProductSearchPageStrategyName.fromString(orderType));

        Page<ProductProjection.JoinRoomAndRegions> productProjectionPage = productSearchPageStrategy.search(params, pageable);
        List<ProductProjection.JoinRoomAndRegions> productProjections = productProjectionPage.getContent();

        List<ProductVo.JoinRoomAndRegions> productVos = productProjections.stream().map(ProductVo.JoinRoomAndRegions::toVo).collect(Collectors.toList());
        return new PageImpl<>(productVos, productProjectionPage.getPageable(), productProjectionPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Object searchListByIds(List<UUID> productIds) {
        List<ProductProjection.JoinRoomAndRegions> productProjections = productService.qSearchListByIdsJoinRoomAndRegions(productIds);
        List<ProductVo.JoinRoomAndRegions> productVos = productProjections.stream().map(ProductVo.JoinRoomAndRegions::toVo).collect(Collectors.toList());
        return productVos;
    }

    /*
    Get login userId
    Get roomEntity
    Compare the userId to the roomEntity.getUserId(). If not equal to both, then throw out the access-denied exception.
    Extract a thumbnail for images.
    Make a new productEntity.
    Make new productImageEntity list.
    Save the productEntity to the database.
    Save the productImageEntities to the database.
     */
    @Transactional
    public void createOne(UUID roomId, ProductDto.Create productDto) {
        UUID userId = userService.getUserIdOrThrow();

        RoomEntity roomEntity = roomService.searchByIdOrThrow(roomId);

        if (!userId.equals(roomEntity.getUserId())) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        List<ProductImageDto.Create> productImageDto = productDto.getProductImages();

        ProductImageDto.Create thumbnailImageDto = productImageDto.get(0);

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
                .deletedFlag(ProductDeletedFlagEnum.EXIST.getValue())
                .createdAt(CustomDateUtils.getCurrentDateTime())
                .updatedAt(CustomDateUtils.getCurrentDateTime())
                .productCategoryId(productDto.getProductCategoryId())
                .roomId(roomEntity.getId())
                .build();

        List<ProductImageEntity> productImageEntities = productImageDto.stream().map(dto -> {
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
        ProductProjection.JoinRoomAndRegions productProjection = productService.qSearchOneByIdJoinRoomAndRegionOrThrow(productId);
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
        ProductProjection.JoinRoomAndRegions productProjection = productService.qSearchOneByIdJoinRoomAndRegionOrThrow(productId);
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
        ProductProjection.JoinRoomAndRegions productProjection = productService.qSearchOneByIdJoinRoomAndRegionOrThrow(productId);
        ProductEntity productEntity = productProjection.getProductEntity();
        RoomEntity roomEntity = productProjection.getRoomEntity();

        if (!userId.equals(roomEntity.getUserId())) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        ProductImageDto.Update thumbnailDto = productDto.getProductImages().stream().findFirst().orElseThrow(() -> new NotMatchedFormatException("이미지는 최소 1개 이상 등록입니다."));
        String thumbnailUri = thumbnailDto.getFileFullUri();

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

        productImageService.logicalDeleteByProductId(productEntity.getId());
        productImageService.saveAll(productImageEntities);

        productEntity.setName(productDto.getName());
        productEntity.setDescription(productDto.getDescription());
        productEntity.setThumbnailUri(thumbnailUri);
        productEntity.setPrice(productDto.getPrice());
        productEntity.setMinimumRentalHour(productDto.getMinimumRentalHour());
        productEntity.setDiscountYn(productDto.getDiscountYn());
        productEntity.setDiscountMinimumHour(productDto.getDiscountMinimumHour());
        productEntity.setDiscountRate(productDto.getDiscountRate());
        productEntity.setDisplayYn(productDto.getDisplayYn());
        productEntity.setUpdatedAt(CustomDateUtils.getCurrentDateTime());
        productEntity.setProductCategoryId(productDto.getProductCategoryId());
    }
}
