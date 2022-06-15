package com.camping_rental.server.domain.order_item.repository;

import com.camping_rental.server.domain.order_info.entity.QOrderInfoEntity;
import com.camping_rental.server.domain.order_item.entity.QOrderItemEntity;
import com.camping_rental.server.domain.order_item.projection.OrderItemProjection;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepositoryCustom {
    private final JPAQueryFactory query;

    private final QOrderItemEntity qOrderItemEntity = QOrderItemEntity.orderItemEntity;
    private final QOrderInfoEntity qOrderInfoEntity = QOrderInfoEntity.orderInfoEntity;

    // TODO : 커버링 인덱스 연구해야됨.
    @Override
    public Page<OrderItemProjection.M2OJ> qSelectM2OJPage(Pageable pageable) {
        JPQLQuery customQuery1 = query.from(qOrderInfoEntity)
                .select(qOrderInfoEntity.id)
                .orderBy(qOrderInfoEntity.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        List<UUID> orderInfoIds = customQuery1.fetch();
        long totalCount = customQuery1.fetchCount();

        JPQLQuery customQuery2 = query.from(qOrderItemEntity)
                .select(
                        Projections.fields(
                                OrderItemProjection.M2OJ.class,
                                qOrderItemEntity.as("orderItemEntity"),
                                qOrderInfoEntity.as("orderInfoEntity")
                        )
                )
                .join(qOrderInfoEntity).on(qOrderInfoEntity.id.eq(qOrderItemEntity.orderInfoId))
                .where(qOrderItemEntity.orderInfoId.in(orderInfoIds));

        List<OrderItemProjection.M2OJ> orderItemProjections = customQuery2.fetch();
        return new PageImpl<>(orderItemProjections, pageable, totalCount);
    }
}
