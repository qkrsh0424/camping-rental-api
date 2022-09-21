package com.camping_rental.server.domain.rental_order_info.repository;

import com.camping_rental.server.domain.enums.DeletedFlagEnums;
import com.camping_rental.server.domain.rental_order_info.entity.QRentalOrderInfoEntity;
import com.camping_rental.server.domain.rental_order_info.entity.RentalOrderInfoEntity;
import com.camping_rental.server.domain.rental_order_info.projection.RentalOrderInfoProjection;
import com.camping_rental.server.domain.rental_order_product.entity.QRentalOrderProductEntity;
import com.camping_rental.server.domain.rental_order_product.entity.RentalOrderProductEntity;
import com.camping_rental.server.domain.room.entity.QRoomEntity;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RentalOrderInfoRepositoryImpl implements RentalOrderInfoRepositoryCustom {
    private final JPAQueryFactory query;

    private final QRentalOrderInfoEntity qRentalOrderInfoEntity = QRentalOrderInfoEntity.rentalOrderInfoEntity;
    private final QRentalOrderProductEntity qRentalOrderProductEntity = QRentalOrderProductEntity.rentalOrderProductEntity;
    private final QRoomEntity qRoomEntity = QRoomEntity.roomEntity;

    @Override
    public Page<RentalOrderInfoProjection.RelatedRoom> qSelectPageByUserIdRelatedRoom(UUID userId, Pageable pageable) {
        JPQLQuery customQuery = query.from(qRentalOrderInfoEntity)
                .select(
                        Projections.fields(
                                RentalOrderInfoProjection.RelatedRoom.class,
                                qRentalOrderInfoEntity.as("rentalOrderInfoEntity"),
                                qRoomEntity.as("roomEntity")
                        )
                )
                .join(qRoomEntity).on(qRoomEntity.id.eq(qRentalOrderInfoEntity.lenderRoomId))
                .where(qRentalOrderInfoEntity.ordererId.eq(userId))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(qRentalOrderInfoEntity.createdAt.desc())
                ;

        long totalCount = customQuery.fetchCount();
        List<RentalOrderInfoProjection.RelatedRoom> rentalOrderInfoProjections = customQuery.fetch();

        return new PageImpl<>(rentalOrderInfoProjections, pageable, totalCount);
    }

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
                );

        Optional<RentalOrderInfoProjection.FullJoin> fetchResult = fetchResults.stream().findFirst();
        return fetchResult;
    }

    @Override
    public Page<RentalOrderInfoProjection.FullJoin> qSelectPageFullJoinByRoomId(UUID roomId, Pageable pageable) {
        JPQLQuery customQuery = query.from(qRentalOrderInfoEntity)
                .select(
                        Projections.fields(
                                RentalOrderInfoProjection.FullJoin.class,
                                qRentalOrderInfoEntity.as("rentalOrderInfoEntity"),
                                qRoomEntity.as("roomEntity")
                        )
                )
                .join(qRoomEntity).on(
                        qRoomEntity.id.eq(qRentalOrderInfoEntity.lenderRoomId)
                                .and(qRoomEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .where(
                        qRentalOrderInfoEntity.lenderRoomId.eq(roomId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qRentalOrderInfoEntity.createdAt.desc())
                .orderBy(qRentalOrderInfoEntity.orderer.asc());

        long totalCount = customQuery.fetchCount();

        List<RentalOrderInfoProjection.FullJoin> rentalOrderInfoProjections = customQuery.fetch();
        List<UUID> rentalOrderInfoIds = rentalOrderInfoProjections.stream().map(r -> r.getRentalOrderInfoEntity().getId()).collect(Collectors.toList());

        List<RentalOrderProductEntity> rentalOrderProductEntities = query.from(qRentalOrderProductEntity)
                .select(qRentalOrderProductEntity)
                .where(qRentalOrderProductEntity.rentalOrderInfoId.in(rentalOrderInfoIds))
                .fetch();

        rentalOrderInfoProjections.forEach(rentalOrderInfoProjection -> {
            rentalOrderProductEntities.forEach(rentalOrderProductEntity -> {
                if (rentalOrderInfoProjection.getRentalOrderInfoEntity().getId().equals(rentalOrderProductEntity.getRentalOrderInfoId())) {
                    rentalOrderInfoProjection.getRentalOrderProductEntities().add(rentalOrderProductEntity);
                }
            });
        });


        return new PageImpl<>(rentalOrderInfoProjections, pageable, totalCount);
    }
}
