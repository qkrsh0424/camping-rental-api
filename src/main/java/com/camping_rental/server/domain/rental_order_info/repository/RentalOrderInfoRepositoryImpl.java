package com.camping_rental.server.domain.rental_order_info.repository;

import com.camping_rental.server.domain.enums.DeletedFlagEnums;
import com.camping_rental.server.domain.rental_order_info.entity.QRentalOrderInfoEntity;
import com.camping_rental.server.domain.rental_order_info.projection.RentalOrderInfoProjection;
import com.camping_rental.server.domain.rental_order_product.entity.QRentalOrderProductEntity;
import com.camping_rental.server.domain.rental_order_product.entity.RentalOrderProductEntity;
import com.camping_rental.server.domain.room.entity.QRoomEntity;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RentalOrderInfoRepositoryImpl implements RentalOrderInfoRepositoryCustom {
    private final JPAQueryFactory query;

    private final QRentalOrderInfoEntity qRentalOrderInfoEntity = QRentalOrderInfoEntity.rentalOrderInfoEntity;
    private final QRentalOrderProductEntity qRentalOrderProductEntity = QRentalOrderProductEntity.rentalOrderProductEntity;
    private final QRoomEntity qRoomEntity = QRoomEntity.roomEntity;

    @Override
    public Optional<RentalOrderInfoProjection.FullJoin> qSelectOneFullJoinByOrderNumberAndOrdererAndOrdererPhoneNumber(String orderNumber, String orderer, String ordererPhoneNumber) {
        List<RentalOrderInfoProjection.FullJoin> fetchResults = query.from(qRentalOrderProductEntity)
                .join(qRentalOrderInfoEntity).on(
                        qRentalOrderInfoEntity.id.eq(qRentalOrderProductEntity.rentalOrderInfoId)
                                .and(qRentalOrderInfoEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .join(qRoomEntity).on(
                        qRoomEntity.id.eq(qRentalOrderInfoEntity.lenderRoomId)
                                .and(qRoomEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .where(
                        qRentalOrderInfoEntity.orderNumber.eq(orderNumber),
                        qRentalOrderInfoEntity.orderer.eq(orderer),
                        qRentalOrderInfoEntity.ordererPhoneNumber.eq(ordererPhoneNumber)
                )
                .transform(
                        GroupBy.groupBy(qRentalOrderInfoEntity.cid)
                                .list(
                                        Projections.fields(
                                                RentalOrderInfoProjection.FullJoin.class,
                                                qRentalOrderInfoEntity.as("rentalOrderInfoEntity"),
                                                qRoomEntity.as("roomEntity"),
                                                GroupBy.set(
                                                    qRentalOrderProductEntity
                                                ).as("rentalOrderProductEntities")
                                        )
                                )
                )
                ;

        Optional<RentalOrderInfoProjection.FullJoin> fetchResult = fetchResults.stream().findFirst();
        return fetchResult;
    }
}
