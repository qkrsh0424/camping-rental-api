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
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
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
    public Optional<ProductProjection.RelatedRoomAndRegions> qSelectOneByIdJoinRoomAndRegion(UUID id) {
        List<ProductProjection.RelatedRoomAndRegions> resultList = query.from(qProductEntity)
                        .join(qRoomEntity).on(
                                qRoomEntity.id.eq(qProductEntity.roomId)
                                        .and(qRoomEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                        )
                        .join(qRegionEntity).on(
                                qRegionEntity.roomId.eq(qRoomEntity.id)
                                        .and(qRegionEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                        )
                        .where(qProductEntity.id.eq(id))
                        .orderBy(qProductEntity.cid.asc())
                        .transform(
                                GroupBy.groupBy(qProductEntity.cid)
                                        .list(
                                                Projections.fields(
                                                        ProductProjection.RelatedRoomAndRegions.class,
                                                        qProductEntity.as("productEntity"),
                                                        qRoomEntity.as("roomEntity"),
                                                        GroupBy.set(
                                                                qRegionEntity
                                                        ).as("regionEntities")
                                                )
                                        )
                        );

        return resultList.stream().findFirst();
    }

    @Override
    public Page<ProductProjection.RelatedRoom> qSelectPageRelatedRoom(Map<String, Object> params, Pageable pageable) {
        Object orderType = params.get("orderType");
        JPQLQuery customQuery = query.from(qProductEntity)
                .select(
                        Projections.fields(
                                ProductProjection.RelatedRoom.class,
                                qProductEntity.as("productEntity"),
                                qRoomEntity.as("roomEntity")
                        )
                )
                .join(qRoomEntity).on(
                        qRoomEntity.id.eq(qProductEntity.roomId)
                                .and(qRoomEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .where(eqCategoryId(params))
                .where(eqRoomId(params))
                .where(eqDisplayYn(params))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                ;

        if (orderType == null || orderType.toString().equals("order_rank")) {
            this.orderByProductRankQuery(customQuery);
        }

        this.sortPagedData(customQuery, pageable);

        List<ProductProjection.RelatedRoom> result = customQuery.fetch();
        long totalCount = customQuery.fetchCount();

        return new PageImpl<>(result, pageable, totalCount);
    }

    @Override
    public Page<ProductProjection.RelatedRoomAndRegions> qSelectPageRelatedRoomAndRegions(Map<String, Object> params, Pageable pageable) {

        JPQLQuery coveringIdxQuery = returnQueryForSearchPageCoveringIdxCids(params, pageable);
        List<Long> cids = coveringIdxQuery.fetch();
        long totalCount = coveringIdxQuery.fetchCount();

        JPQLQuery productProjectionsQuery = query.from(qProductEntity)
                .select(
                        Projections.fields(
                                ProductProjection.RelatedRoomAndRegions.class,
                                qProductEntity.as("productEntity"),
                                qRoomEntity.as("roomEntity")
                        )
                )
                .join(qRoomEntity).on(
                        qRoomEntity.id.eq(qProductEntity.roomId)
                                .and(qRoomEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .where(qProductEntity.cid.in(cids))
                .orderBy(orderByFieldList(qProductEntity.cid, cids, "asc"));

        List<ProductProjection.RelatedRoomAndRegions> productProjections = productProjectionsQuery.fetch();

        JPQLQuery regionsQuery = query.from(qRegionEntity)
                .select(qRegionEntity)
                .where(qRegionEntity.roomId.in(productProjections.stream().map(r -> r.getRoomEntity().getId()).collect(Collectors.toSet())));

        List<RegionEntity> allRegionEntities = regionsQuery.fetch();
        productProjections.forEach(proj -> {
            List<RegionEntity> regionEntites = allRegionEntities.stream().filter(r -> r.getRoomId().equals(proj.getRoomEntity().getId())).collect(Collectors.toList());
            proj.setRegionEntities(regionEntites.stream().collect(Collectors.toSet()));
        });

        return new PageImpl<>(productProjections, pageable, totalCount);
    }

    @Override
    public Optional<ProductProjection.RelatedProductCategoryAndRoomAndRegionsAndProductImages> qSelectByIdRelatedProductCategoryAndRoomAndRegionsAndProductImages(UUID productId, Map<String, Object> params) {
        List<ProductProjection.RelatedProductCategoryAndRoomAndRegionsAndProductImages> productProjections = query.from(qProductImageEntity)
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
                .where(eqDisplayYn(params))
                .orderBy(qProductEntity.cid.asc())
                .orderBy(qProductImageEntity.cid.asc())
                .orderBy(qRegionEntity.cid.asc())
                .transform(
                        GroupBy.groupBy(qProductEntity.cid)
                                .list(
                                        Projections.fields(
                                                ProductProjection.RelatedProductCategoryAndRoomAndRegionsAndProductImages.class,
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
    public List<ProductProjection.RelatedRoomAndRegions> qSelectListByIdsRelatedRoomAndRegions(List<UUID> productIds) {
        JPQLQuery productProjectionsQuery = query.from(qProductEntity)
                .select(
                        Projections.fields(
                                ProductProjection.RelatedRoomAndRegions.class,
                                qProductEntity.as("productEntity"),
                                qRoomEntity.as("roomEntity")
                        )
                )
                .join(qRoomEntity).on(
                        qRoomEntity.id.eq(qProductEntity.roomId)
                                .and(qRoomEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .where(qProductEntity.id.in(productIds));

        List<ProductProjection.RelatedRoomAndRegions> productProjections = productProjectionsQuery.fetch();

        JPQLQuery regionsQuery = query.from(qRegionEntity)
                .select(qRegionEntity)
                .where(qRegionEntity.roomId.in(productProjections.stream().map(r -> r.getRoomEntity().getId()).collect(Collectors.toList())));

        List<RegionEntity> allRegionEntities = regionsQuery.fetch();
        productProjections.forEach(proj -> {
            List<RegionEntity> regionEntites = allRegionEntities.stream().filter(r -> r.getRoomId().equals(proj.getRoomEntity().getId())).collect(Collectors.toList());
            proj.setRegionEntities(regionEntites.stream().collect(Collectors.toSet()));
        });

        return productProjections;
    }

    private JPQLQuery returnQueryForSearchPageCoveringIdxCids(Map<String, Object> params, Pageable pageable) {
        Object orderType = params.get("orderType");
        JPQLQuery customQuery = query.from(qProductEntity)
                .select(qProductEntity.cid)
                .join(qRoomEntity).on(
                        qRoomEntity.id.eq(qProductEntity.roomId)
                                .and(qRoomEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .where(eqCategoryId(params))
                .where(eqRoomId(params))
                .where(eqDisplayYn(params))
                .where(eqRoomIdWithRegionSubquery(params))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if (orderType == null || orderType.toString().equals("order_rank")) {
            this.orderByProductRankQuery(customQuery);
        }

        this.sortPagedData(customQuery, pageable);

        return customQuery;
    }

    private void orderByProductRankQuery(JPQLQuery customQuery){
        customQuery
                .leftJoin(qRentalOrderProductEntity)
                .on(
                        qRentalOrderProductEntity.productId.eq(qProductEntity.id)
                                .and(qRentalOrderProductEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .leftJoin(qProductCountInfoEntity)
                .on(
                        qProductCountInfoEntity.productId.eq(qProductEntity.id)
                )
                .groupBy(qProductEntity.cid)
                .orderBy(
                        qRentalOrderProductEntity.cid.count()
                                .multiply(10)
                                .add(
                                        qProductCountInfoEntity.viewCount.coalesce(0)
                                )
                                .desc()
                );
    }

    private BooleanExpression eqRoomIdWithRegionSubquery(Map<String, Object> params) {
        return qRoomEntity.id.eq(
                query.select(qRegionEntity.roomId)
                        .from(qRegionEntity)
                        .where(eqRegionSido(params))
                        .where(eqRegionSigungu(params))
                        .where(qRegionEntity.roomId.eq(qRoomEntity.id))
                        .groupBy(qRegionEntity.roomId)
        );
    }

    private BooleanExpression eqRegionSido(Map<String, Object> params) {
        String sido = null;

        try {
            sido = params.get("sido").toString();
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }

        return qRegionEntity.sido.eq(sido);
    }

    private BooleanExpression eqRegionSigungu(Map<String, Object> params) {
        String sigungu = null;

        try {
            sigungu = params.get("sigungu").toString();
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }

        return qRegionEntity.sigungu.eq(sigungu);
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

    private <T, E> OrderSpecifier<?> orderByFieldList(T field, List<E> fieldList, String orderBy) {
        StringTemplate stringTemplate =Expressions.stringTemplate("FIELD({0}, {1})", field, fieldList);

        if(orderBy.equals("asc")){
            return stringTemplate.asc();
        }else{
            return stringTemplate.desc();
        }
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
