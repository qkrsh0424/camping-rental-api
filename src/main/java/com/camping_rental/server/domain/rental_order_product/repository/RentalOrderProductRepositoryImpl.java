package com.camping_rental.server.domain.rental_order_product.repository;

import com.camping_rental.server.domain.enums.DeletedFlagEnums;
import com.camping_rental.server.domain.rental_order_info.entity.QRentalOrderInfoEntity;
import com.camping_rental.server.domain.rental_order_product.entity.QRentalOrderProductEntity;
import com.camping_rental.server.domain.rental_order_product.projection.CountProductsProjection;
import com.camping_rental.server.domain.rental_order_product.projection.RentalOrderProductProjection;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Repository
@RequiredArgsConstructor
public class RentalOrderProductRepositoryImpl implements RentalOrderProductRepositoryCustom {
    private final JPAQueryFactory query;

    private final QRentalOrderProductEntity qRentalOrderProductEntity = QRentalOrderProductEntity.rentalOrderProductEntity;
    private final QRentalOrderInfoEntity qRentalOrderInfoEntity = QRentalOrderInfoEntity.rentalOrderInfoEntity;


    @Override
    public Page<RentalOrderProductProjection.JoinRentalOrderInfo> qSelectPageJoinRentalOrderInfo(Map<String, Object> params, Pageable pageable) {
        JPQLQuery customQuery = query.from(qRentalOrderProductEntity)
                .select(
                        Projections.fields(
                                RentalOrderProductProjection.JoinRentalOrderInfo.class,
                                qRentalOrderProductEntity.as("rentalOrderProductEntity"),
                                qRentalOrderInfoEntity.as("rentalOrderInfoEntity")
                        )
                )
                .join(qRentalOrderInfoEntity).on(
                        qRentalOrderInfoEntity.id.eq(qRentalOrderProductEntity.rentalOrderInfoId)
                                .and(qRentalOrderInfoEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(eqRoomId(params))
                .where(eqStatus(params));

        this.sortPagedData(customQuery, pageable);

        customQuery.orderBy(qRentalOrderInfoEntity.orderer.asc());
        customQuery.orderBy(qRentalOrderInfoEntity.cid.asc());

        long totalCount = customQuery.fetchCount();

        List<RentalOrderProductProjection.JoinRentalOrderInfo> rentalOrderProductProjections = customQuery.fetch();

        return new PageImpl<>(rentalOrderProductProjections, pageable, totalCount);
    }

    @Override
    public List<RentalOrderProductProjection.JoinRentalOrderInfo> qSelectListByIdsJoinRentalOrderInfo(List<UUID> ids) {
        JPQLQuery customQuery = query.from(qRentalOrderProductEntity)
                .select(
                        Projections.fields(
                                RentalOrderProductProjection.JoinRentalOrderInfo.class,
                                qRentalOrderProductEntity.as("rentalOrderProductEntity"),
                                qRentalOrderInfoEntity.as("rentalOrderInfoEntity")
                        )
                )
                .join(qRentalOrderInfoEntity).on(
                        qRentalOrderInfoEntity.id.eq(qRentalOrderProductEntity.rentalOrderInfoId)
                                .and(qRentalOrderInfoEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .where(qRentalOrderProductEntity.id.in(ids))
                ;


        List<RentalOrderProductProjection.JoinRentalOrderInfo> rentalOrderProductProjections = customQuery.fetch();

        return rentalOrderProductProjections;
    }

    @Override
    public List<CountProductsProjection.Product> qCountProductsByRoomId(UUID roomId, LocalDateTime startDate, LocalDateTime endDate, List<String> orderTypes) {
        JPQLQuery customQuery = query.from(qRentalOrderProductEntity)
                .select(
                        Projections.fields(
                                CountProductsProjection.Product.class,
                                qRentalOrderProductEntity.productId.as("productId"),
                                qRentalOrderProductEntity.productName.as("productName"),
                                qRentalOrderProductEntity.thumbnailUri.as("thumbnailUri"),
                                qRentalOrderProductEntity.unit.sum().as("unitSum")
                        )
                )
                .join(qRentalOrderInfoEntity).on(
                        qRentalOrderInfoEntity.id.eq(qRentalOrderProductEntity.rentalOrderInfoId)
                                .and(qRentalOrderInfoEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .where(qRentalOrderInfoEntity.lenderRoomId.eq(roomId))
                .where(
                        /*픽업 날짜가 조회 기간내에 있을때*/
                        qRentalOrderInfoEntity.pickupDate.between(startDate, endDate)
                                /*반납 날짜가 조회 기간내에 있을때*/
                                .or(qRentalOrderInfoEntity.returnDate.between(startDate, endDate))
                                /*픽업 날짜와 반납 날짜 사이에 조회 기간이 있을떄*/
                                .or(qRentalOrderInfoEntity.pickupDate.before(startDate).and(qRentalOrderInfoEntity.returnDate.after(endDate)))
                )
                .where(qRentalOrderProductEntity.status.in(orderTypes))
                .groupBy(qRentalOrderProductEntity.productName)
                ;

        List<CountProductsProjection.Product> countProducts = customQuery.fetch();

        return countProducts;
    }

    @Override
    public Optional<RentalOrderProductProjection.JoinRentalOrderInfo> qSelectByIdAndLenderRoomIdJoinRentalOrderInfo(UUID id, UUID roomId) {
        JPQLQuery<RentalOrderProductProjection.JoinRentalOrderInfo> customQuery = query.from(qRentalOrderProductEntity)
                .select(
                        Projections.fields(
                                RentalOrderProductProjection.JoinRentalOrderInfo.class,
                                qRentalOrderProductEntity.as("rentalOrderProductEntity"),
                                qRentalOrderInfoEntity.as("rentalOrderInfoEntity")
                        )
                )
                .join(qRentalOrderInfoEntity).on(
                        qRentalOrderInfoEntity.id.eq(qRentalOrderProductEntity.rentalOrderInfoId)
                                .and(qRentalOrderInfoEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .where(qRentalOrderProductEntity.id.eq(id))
                .where(qRentalOrderInfoEntity.lenderRoomId.eq(roomId))
                ;

        return Optional.of(customQuery.fetchOne());
    }

    private BooleanExpression eqRoomId(Map<String, Object> params) {
        Object roomIdObj = params.get("roomId");
        UUID roomId = null;

        try {
            roomId = UUID.fromString(roomIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }

        return qRentalOrderInfoEntity.lenderRoomId.eq(roomId);
    }

    private BooleanExpression eqStatus(Map<String, Object> params) {
        Object statusObj = params.get("status");
        String status = null;

        try {
            status = statusObj.toString();
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }

        return qRentalOrderProductEntity.status.eq(status);
    }

    private void sortPagedData(JPQLQuery customQuery, Pageable pageable) {
        PathBuilder rentalOrderProduct = new PathBuilder(qRentalOrderProductEntity.getType(), qRentalOrderProductEntity.getMetadata());
        PathBuilder rentalOrderInfo = new PathBuilder(qRentalOrderInfoEntity.getType(), qRentalOrderInfoEntity.getMetadata());

        for (Sort.Order o : pageable.getSort()) {

            switch (o.getProperty().toString()) {
                case "cid":
                    customQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, rentalOrderProduct.get("cid")));
                    break;
                case "createdAt":
                    customQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, rentalOrderInfo.get("createdAt")));
                    break;
                default:
                    customQuery.orderBy(qRentalOrderProductEntity.cid.desc());
            }
        }
    }
}
