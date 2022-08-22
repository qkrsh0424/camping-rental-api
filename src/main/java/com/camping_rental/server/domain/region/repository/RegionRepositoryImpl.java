package com.camping_rental.server.domain.region.repository;

import com.camping_rental.server.domain.enums.DeletedFlagEnums;
import com.camping_rental.server.domain.region.entity.QRegionEntity;
import com.camping_rental.server.domain.region.entity.RegionEntity;
import com.camping_rental.server.domain.region.projection.RegionProjection;
import com.camping_rental.server.domain.room.entity.QRoomEntity;
import com.camping_rental.server.domain.user.entity.QUserEntity;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RegionRepositoryImpl implements RegionRepositoryCustom {
    private final JPAQueryFactory query;

    private final QUserEntity qUserEntity = QUserEntity.userEntity;
    private final QRoomEntity qRoomEntity = QRoomEntity.roomEntity;
    private final QRegionEntity qRegionEntity = QRegionEntity.regionEntity;

    @Override
    public List<RegionEntity> qSelectByUserId(UUID userId) {
        JPQLQuery customQuery = query.from(qRegionEntity)
                .select(qRegionEntity)
                .join(qRoomEntity).on(
                        qRoomEntity.cid.eq(qRegionEntity.roomCid)
                                .and(qRoomEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .where(qRoomEntity.userId.eq(userId));

        return customQuery.fetch();
    }

    @Override
    public List<RegionProjection.SidoAndSigungus> qSelectListSidoAndSigungus() {
        List<RegionProjection.SidoAndSigungus> sidoAndSigungusProjections = query.from(qRegionEntity)
                .orderBy(qRegionEntity.sido.asc())
                .transform(
                        GroupBy.groupBy(qRegionEntity.sido)
                                .list(
                                        Projections.fields(
                                                RegionProjection.SidoAndSigungus.class,
                                                qRegionEntity.sido.as("sido"),
                                                GroupBy.set(
                                                        qRegionEntity.sigungu
                                                ).as("sigungus")
                                        )
                                )
                );
        return sidoAndSigungusProjections;
    }
}
