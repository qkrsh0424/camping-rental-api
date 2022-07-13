package com.camping_rental.server.domain.product_category.repository;

import com.camping_rental.server.domain.enums.DeletedFlagEnums;
import com.camping_rental.server.domain.order_info.projection.OrderInfoProjection;
import com.camping_rental.server.domain.product.entity.QProductEntity;
import com.camping_rental.server.domain.product_category.entity.ProductCategoryEntity;
import com.camping_rental.server.domain.product_category.entity.QProductCategoryEntity;
import com.camping_rental.server.domain.room.entity.QRoomEntity;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductCategoryRepositoryImpl implements ProductCategoryRepositoryCustom{
    private final JPAQueryFactory query;
    private final QProductCategoryEntity qProductCategoryEntity = QProductCategoryEntity.productCategoryEntity;
    private final QProductEntity qProductEntity = QProductEntity.productEntity;
    private final QRoomEntity qRoomEntity = QRoomEntity.roomEntity;

    @Override
    public List<ProductCategoryEntity> qSelectListByRoomId(UUID roomId) {
        JPQLQuery customQuery = query.from(qProductEntity)
                .select(qProductCategoryEntity)
                .join(qRoomEntity).on(
                        qRoomEntity.id.eq(qProductEntity.roomId)
                                .and(qRoomEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .join(qProductCategoryEntity).on(
                        qProductCategoryEntity.id.eq(qProductEntity.productCategoryId)
                                .and(qProductCategoryEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .where(qProductEntity.roomId.eq(roomId))
                .groupBy(qProductCategoryEntity.cid)
                ;

        List<ProductCategoryEntity> productCategoryEntities = customQuery.fetch();
        return productCategoryEntities;
    }
}
