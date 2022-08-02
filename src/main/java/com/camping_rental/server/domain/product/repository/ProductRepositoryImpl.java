package com.camping_rental.server.domain.product.repository;

import com.camping_rental.server.domain.enums.DeletedFlagEnums;
import com.camping_rental.server.domain.product.entity.QProductEntity;
import com.camping_rental.server.domain.product.projection.ProductProjection;
import com.camping_rental.server.domain.product_category.entity.QProductCategoryEntity;
import com.camping_rental.server.domain.product_count_info.entity.QProductCountInfoEntity;
import com.camping_rental.server.domain.product_image.entity.QProductImageEntity;
import com.camping_rental.server.domain.region.entity.QRegionEntity;
import com.camping_rental.server.domain.region.entity.RegionEntity;
import com.camping_rental.server.domain.rental_order_product.entity.QRentalOrderProductEntity;
import com.camping_rental.server.domain.room.entity.QRoomEntity;
import com.camping_rental.server.utils.CustomFieldUtils;
import com.querydsl.core.QueryException;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory query;

    private final QProductEntity qProductEntity = QProductEntity.productEntity;
    private final QRoomEntity qRoomEntity = QRoomEntity.roomEntity;
    private final QRegionEntity qRegionEntity = QRegionEntity.regionEntity;
    private final QProductImageEntity qProductImageEntity = QProductImageEntity.productImageEntity;
    private final QProductCategoryEntity qProductCategoryEntity = QProductCategoryEntity.productCategoryEntity;
    private final QRentalOrderProductEntity qRentalOrderProductEntity = QRentalOrderProductEntity.rentalOrderProductEntity;
    private final QProductCountInfoEntity qProductCountInfoEntity = QProductCountInfoEntity.productCountInfoEntity;

    @Override
    public Optional<ProductProjection.JoinRoomAndRegions> qSelectOneByIdJoinRoomAndRegion(UUID id) {
        JPQLQuery customQuery = query.from(qProductEntity)
                .select(
                        Projections.fields(
                                ProductProjection.JoinRoomAndRegions.class,
                                qProductEntity.as("productEntity"),
                                qRoomEntity.as("roomEntity")
                        )
                )
                .join(qRoomEntity).on(
                        qRoomEntity.id.eq(qProductEntity.roomId)
                                .and(qRoomEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .where(qProductEntity.id.eq(id));
        ProductProjection.JoinRoomAndRegions result = (ProductProjection.JoinRoomAndRegions) customQuery.fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<ProductProjection.JoinRoomAndRegions> qSelectListJoinRoomAndRegions(UUID roomId, Map<String, Object> params) {
        List<ProductProjection.JoinRoomAndRegions> productProjections = query.from(qProductEntity)
                .join(qRoomEntity).on(
                        qRoomEntity.id.eq(qProductEntity.roomId)
                                .and(qRoomEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .join(qRegionEntity).on(
                        qRegionEntity.roomId.eq(qRoomEntity.id)
                                .and(qRegionEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .where(qRoomEntity.id.eq(roomId))
                .where(eqCategoryId(params))
                .transform(
                        GroupBy.groupBy(qProductEntity.cid)
                                .list(
                                        Projections.fields(
                                                ProductProjection.JoinRoomAndRegions.class,
                                                qProductEntity.as("productEntity"),
                                                qRoomEntity.as("roomEntity"),
                                                GroupBy.list(
                                                        qRegionEntity
                                                ).as("regionEntities")
                                        )
                                )
                );

        return productProjections;
    }

    @Override
    public List<ProductProjection.JoinRoomAndRegions> qSelectListJoinRoomAndRegions() {
        List<ProductProjection.JoinRoomAndRegions> productProjections = query.from(qProductEntity)
                .join(qRoomEntity).on(
                        qRoomEntity.id.eq(qProductEntity.roomId)
                                .and(qRoomEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .join(qRegionEntity).on(
                        qRegionEntity.roomId.eq(qRoomEntity.id)
                                .and(qRegionEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .transform(
                        GroupBy.groupBy(qProductEntity.cid)
                                .list(
                                        Projections.fields(
                                                ProductProjection.JoinRoomAndRegions.class,
                                                qProductEntity.as("productEntity"),
                                                qRoomEntity.as("roomEntity"),
                                                GroupBy.list(
                                                        qRegionEntity
                                                ).as("regionEntities")
                                        )
                                )
                );

        return productProjections;
    }

    @Override
    public Page<ProductProjection.JoinRoomAndRegions> qSelectPageJoinRoomAndRegions(Map<String, Object> params, Pageable pageable) {
        JPQLQuery productProjectionsQuery = query.from(qProductEntity)
                .select(
                        Projections.fields(
                                ProductProjection.JoinRoomAndRegions.class,
                                qProductEntity.as("productEntity"),
                                qRoomEntity.as("roomEntity")
                        )
                )
                .join(qRoomEntity).on(
                        qRoomEntity.id.eq(qProductEntity.roomId)
                                .and(qRoomEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(eqCategoryId(params))
                .where(eqRoomId(params))
                .where(eqDisplayYn(params));

        sortPagedData(productProjectionsQuery, pageable);

        long totalCount = productProjectionsQuery.fetchCount();
        List<ProductProjection.JoinRoomAndRegions> productProjections = productProjectionsQuery.fetch();

        JPQLQuery regionsQuery = query.from(qRegionEntity)
                .select(qRegionEntity)
                .where(qRegionEntity.roomId.in(productProjections.stream().map(r -> r.getRoomEntity().getId()).collect(Collectors.toList())));

        List<RegionEntity> allRegionEntities = regionsQuery.fetch();
        productProjections.forEach(proj -> {
            List<RegionEntity> regionEntites = allRegionEntities.stream().filter(r -> r.getRoomId().equals(proj.getRoomEntity().getId())).collect(Collectors.toList());
            proj.setRegionEntities(regionEntites);
        });

        return new PageImpl<>(productProjections, pageable, totalCount);
    }

    @Override
    public Page<ProductProjection.JoinRoomAndRegions> qSelectPageJoinRoomAndRegions2(Map<String, Object> params, Pageable pageable) {
        JPQLQuery productProjectionsQuery = query.from(qProductEntity)
                .select(
                        Projections.fields(
                                ProductProjection.JoinRoomAndRegions.class,
                                qProductEntity.as("productEntity"),
                                qRoomEntity.as("roomEntity")
                        )
                )
                .join(qRoomEntity).on(
                        qRoomEntity.id.eq(qProductEntity.roomId)
                                .and(qRoomEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .leftJoin(qRentalOrderProductEntity).on(qRentalOrderProductEntity.productId.eq(qProductEntity.id))
                .leftJoin(qProductCountInfoEntity).on(qProductCountInfoEntity.productId.eq(qProductEntity.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(eqCategoryId(params))
                .where(eqRoomId(params))
                .where(eqDisplayYn(params))
                .groupBy(qProductEntity.cid)
                .orderBy(
                        qRentalOrderProductEntity.cid.count()
                                .multiply(10)
                                .add(
                                        qProductCountInfoEntity.viewCount.coalesce(0).multiply(1)
                                )
                                .desc(),
                        qProductEntity.cid.asc()
                )
                ;

//        sortPagedData(productProjectionsQuery, pageable);

        long totalCount = productProjectionsQuery.fetchCount();
        List<ProductProjection.JoinRoomAndRegions> productProjections = productProjectionsQuery.fetch();

        JPQLQuery regionsQuery = query.from(qRegionEntity)
                .select(qRegionEntity)
                .where(qRegionEntity.roomId.in(productProjections.stream().map(r -> r.getRoomEntity().getId()).collect(Collectors.toList())));

        List<RegionEntity> allRegionEntities = regionsQuery.fetch();
        productProjections.forEach(proj -> {
            List<RegionEntity> regionEntites = allRegionEntities.stream().filter(r -> r.getRoomId().equals(proj.getRoomEntity().getId())).collect(Collectors.toList());
            proj.setRegionEntities(regionEntites);
        });

        return new PageImpl<>(productProjections, pageable, totalCount);
    }

    @Override
    public Optional<ProductProjection.FullJoin> qSelectOneFullJoin(UUID productId) {
        List<ProductProjection.FullJoin> productProjections = query.from(qProductImageEntity)
                .join(qProductEntity).on(
                        qProductEntity.id.eq(qProductImageEntity.productId)
                                .and(qProductEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .join(qProductCategoryEntity).on(
                        qProductCategoryEntity.id.eq(qProductEntity.productCategoryId)
                                .and(qProductCategoryEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .join(qRoomEntity).on(
                        qRoomEntity.id.eq(qProductEntity.roomId)
                                .and(qRoomEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .join(qRegionEntity).on(
                        qRegionEntity.roomId.eq(qRoomEntity.id)
                                .and(qRegionEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .where(qProductEntity.id.eq(productId))
                .orderBy(qProductEntity.cid.asc())
                .orderBy(qProductImageEntity.cid.asc())
                .orderBy(qRegionEntity.cid.asc())
                .transform(
                        GroupBy.groupBy(qProductEntity.cid)
                                .list(
                                        Projections.fields(
                                                ProductProjection.FullJoin.class,
                                                qRoomEntity.as("roomEntity"),
                                                qProductCategoryEntity.as("productCategoryEntity"),
                                                qProductEntity.as("productEntity"),
                                                GroupBy.set(
                                                        qRegionEntity
                                                ).as("regionEntities"),
                                                GroupBy.set(
                                                        qProductImageEntity
                                                ).as("productImageEntities")

                                        )
                                )

                );

        return productProjections.stream().findFirst();
    }

    @Override
    public List<ProductProjection.JoinRoomAndRegions> qSelectListByIdsJoinRoomAndRegions(List<UUID> productIds) {
        JPQLQuery productProjectionsQuery = query.from(qProductEntity)
                .select(
                        Projections.fields(
                                ProductProjection.JoinRoomAndRegions.class,
                                qProductEntity.as("productEntity"),
                                qRoomEntity.as("roomEntity")
                        )
                )
                .join(qRoomEntity).on(
                        qRoomEntity.id.eq(qProductEntity.roomId)
                                .and(qRoomEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .where(qProductEntity.id.in(productIds));

        List<ProductProjection.JoinRoomAndRegions> productProjections = productProjectionsQuery.fetch();

        JPQLQuery regionsQuery = query.from(qRegionEntity)
                .select(qRegionEntity)
                .where(qRegionEntity.roomId.in(productProjections.stream().map(r -> r.getRoomEntity().getId()).collect(Collectors.toList())));

        List<RegionEntity> allRegionEntities = regionsQuery.fetch();
        productProjections.forEach(proj -> {
            List<RegionEntity> regionEntites = allRegionEntities.stream().filter(r -> r.getRoomId().equals(proj.getRoomEntity().getId())).collect(Collectors.toList());
            proj.setRegionEntities(regionEntites);
        });

        return productProjections;
    }

    private BooleanExpression eqRoomId(Map<String, Object> params) {
        Object roomIdObj = params.get("roomId");
        UUID roomId = null;

        try {
            roomId = UUID.fromString(roomIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }

        return qProductEntity.roomId.eq(roomId);
    }

    private BooleanExpression eqCategoryId(Map<String, Object> params) {
        Object categoryIdObj = params.get("categoryId");

        if (categoryIdObj == null) {
            return null;
        }

        UUID categoryId = null;
        try {
            categoryId = UUID.fromString(categoryIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }

        return qProductEntity.productCategoryId.eq(categoryId);
    }

    private BooleanExpression eqDisplayYn(Map<String, Object> params) {
        Object displayYnObj = params.get("displayYn");

        if (displayYnObj == null) {
            return null;
        }

        String displayYn = null;
        try {
            displayYn = displayYnObj.toString();
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }

        return qProductEntity.displayYn.eq(displayYn);
    }

    private void sortPagedData(JPQLQuery customQuery, Pageable pageable) {
        PathBuilder productBuilder = new PathBuilder(qProductEntity.getType(), qProductEntity.getMetadata());
        PathBuilder productCategoryBuilder = new PathBuilder(qProductCategoryEntity.getType(), qProductCategoryEntity.getMetadata());

        for (Sort.Order o : pageable.getSort()) {

            switch (o.getProperty().toString()) {
                case "cid":
                    customQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, productBuilder.get("cid")));
                    break;
                case "createdAt":
                    customQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, productBuilder.get("createdAt")));
                    break;
                default:
                    customQuery.orderBy(qProductEntity.cid.desc());
            }
        }
    }
}
