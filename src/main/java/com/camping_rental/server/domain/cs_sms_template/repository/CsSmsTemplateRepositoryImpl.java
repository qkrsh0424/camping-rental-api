package com.camping_rental.server.domain.cs_sms_template.repository;

import com.camping_rental.server.domain.cs_sms_template.entity.CsSmsTemplateEntity;
import com.camping_rental.server.domain.cs_sms_template.entity.QCsSmsTemplateEntity;
import com.camping_rental.server.domain.enums.DeletedFlagEnums;
import com.camping_rental.server.domain.room.entity.QRoomEntity;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CsSmsTemplateRepositoryImpl implements CsSmsTemplateRepositoryCustom{
    private final JPAQueryFactory query;

    private final QRoomEntity qRoomEntity = QRoomEntity.roomEntity;
    private final QCsSmsTemplateEntity qCsSmsTemplateEntity = QCsSmsTemplateEntity.csSmsTemplateEntity;

    @Override
    public Page<CsSmsTemplateEntity> qSelectPageByRoomId(UUID roomId, Pageable pageable) {
        JPQLQuery customQuery = query.from(qCsSmsTemplateEntity)
                .select(qCsSmsTemplateEntity)
                .join(qRoomEntity).on(
                        qRoomEntity.id.eq(qCsSmsTemplateEntity.roomId)
                                .and(qRoomEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qCsSmsTemplateEntity.cid.asc());

        long totalCount = customQuery.fetchCount();

        List<CsSmsTemplateEntity> csSmsTemplateEntities = customQuery.fetch();

        return new PageImpl<>(csSmsTemplateEntities, pageable, totalCount);
    }
}
