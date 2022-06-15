package com.camping_rental.server.domain.order_info.repository;

import com.camping_rental.server.domain.item.entity.QItemEntity;
import com.camping_rental.server.domain.order_info.entity.OrderInfoEntity;
import com.camping_rental.server.domain.order_info.entity.QOrderInfoEntity;
import com.camping_rental.server.domain.order_info.projection.OrderInfoProjection;
import com.camping_rental.server.domain.order_item.entity.QOrderItemEntity;
import com.querydsl.core.QueryResults;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderInfoRepositoryImpl implements OrderInfoRepositoryCustom {
    private final JPAQueryFactory query;

    private final QOrderInfoEntity qOrderInfoEntity = QOrderInfoEntity.orderInfoEntity;
    private final QOrderItemEntity qOrderItemEntity = QOrderItemEntity.orderItemEntity;
    private final QItemEntity qItemEntity = QItemEntity.itemEntity;


    @Override
    public List<OrderInfoEntity> qSelectList() {
        JPQLQuery customQuery = query.from(qOrderInfoEntity)
                .select(qOrderInfoEntity);

        QueryResults<OrderInfoEntity> results = customQuery.fetchResults();
        return results.getResults();
    }

    @Override
    public Page<OrderInfoProjection.JoinOrderItems> qSelectJoinOrderItemsPage(Pageable pageable) {
        JPQLQuery customQuery1 = query.from(qOrderInfoEntity)
                .select(qOrderInfoEntity.id)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        List<UUID> orderInfoIds = customQuery1.fetch();
        long orderInfoTotalCount = customQuery1.fetchCount();

        List<OrderInfoProjection.JoinOrderItems> results = query.from(qOrderItemEntity)
                .join(qOrderInfoEntity).on(qOrderInfoEntity.id.eq(qOrderItemEntity.orderInfoId))
                .where(qOrderItemEntity.orderInfoId.in(orderInfoIds))
                .transform(
                        GroupBy.groupBy(qOrderInfoEntity.id)
                                .list(
                                        Projections.fields(
                                                OrderInfoProjection.JoinOrderItems.class,
                                                Projections.fields(
                                                        OrderInfoProjection.OrderInfoPO.class,
                                                        qOrderInfoEntity.id,
                                                        qOrderInfoEntity.status,
                                                        qOrderInfoEntity.orderer,
                                                        qOrderInfoEntity.ordererPhoneNumber,
                                                        qOrderInfoEntity.pickupDate,
                                                        qOrderInfoEntity.returnDate,
                                                        qOrderInfoEntity.pickupRegion,
                                                        qOrderInfoEntity.returnRegion,
                                                        qOrderInfoEntity.pickupTime,
                                                        qOrderInfoEntity.returnTime,
                                                        qOrderInfoEntity.createdAt
                                                ).as("orderInfoPO"),
                                                GroupBy.list(
                                                        Projections.fields(
                                                                OrderInfoProjection.OrderItemPO.class,
                                                                qOrderItemEntity.id,
                                                                qOrderItemEntity.itemName,
                                                                qOrderItemEntity.categoryName,
                                                                qOrderItemEntity.thumbnailFullUri,
                                                                qOrderItemEntity.price,
                                                                qOrderItemEntity.discountRate,
                                                                qOrderItemEntity.unit,
                                                                qOrderItemEntity.nights,
                                                                qOrderItemEntity.beforeDiscountPrice,
                                                                qOrderItemEntity.afterDiscountPrice,
                                                                qOrderItemEntity.adoptedDiscountYn,
                                                                qOrderItemEntity.itemId
                                                        )
                                                ).as("orderItemPOs")
                                        )
                                )
                );

        return new PageImpl<>(results, pageable, orderInfoTotalCount);
    }
}
