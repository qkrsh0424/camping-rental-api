package com.camping_rental.server.domain.product.repository;

import com.camping_rental.server.domain.enums.DeletedFlagEnums;
import com.camping_rental.server.domain.product.entity.QProductEntity;
import com.camping_rental.server.domain.product_category.entity.QProductCategoryEntity;
import com.camping_rental.server.domain.product_count_info.entity.QProductCountInfoEntity;
import com.camping_rental.server.domain.product_image.entity.QProductImageEntity;
import com.camping_rental.server.domain.region.entity.QRegionEntity;
import com.camping_rental.server.domain.rental_order_product.entity.QRentalOrderProductEntity;
import com.camping_rental.server.domain.room.entity.QRoomEntity;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
class ProductRepositoryImplTest {
    @Autowired
    private JPAQueryFactory query;
    QProductEntity qProductEntity = new QProductEntity("productEntity1");
    QRoomEntity qRoomEntity = new QRoomEntity("roomEntity");
    QRentalOrderProductEntity qRentalOrderProductEntity = new QRentalOrderProductEntity("rentalOrderProductEntity");
    QProductCountInfoEntity qProductCountInfoEntity = new QProductCountInfoEntity("productCountInfoEntity");
    QRegionEntity qRegionEntity = new QRegionEntity("regionEntity");

    @Test
    public void getCoveringIndexForSearchTest() {

        JPQLQuery customQuery = query.from(qProductEntity)
                .select(qProductEntity.cid)
                .join(qRoomEntity).on(
                        qRoomEntity.id.eq(qProductEntity.roomId)
                                .and(qRoomEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .leftJoin(qRentalOrderProductEntity)
                .on(
                        qRentalOrderProductEntity.productId.eq(qProductEntity.id)
                                .and(qRentalOrderProductEntity.deletedFlag.eq(DeletedFlagEnums.EXIST.getValue()))
                )
                .leftJoin(qProductCountInfoEntity)
                .on(
                        qProductCountInfoEntity.productId.eq(qProductEntity.id)
                )
                .where(
                        qRoomEntity.id.eq(
                                query.select(qRegionEntity.roomId)
                                        .from(qRegionEntity)
                                        .where(qRegionEntity.sido.eq("경남"))
                                        .where(qRegionEntity.roomId.eq(qRoomEntity.id))
                                        .groupBy(qRegionEntity.roomId)
                        )
                )
                .groupBy(qProductEntity.cid)
                ;

        System.out.println(customQuery.fetch());
    }

    private BooleanExpression eqRegionSido(Map<String, Object> params) {
        Object sidoObj = params.get("sido");
        String sido = null;

        try {
            sido = sidoObj.toString();
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }

        return qRegionEntity.sido.eq(sido);
    }
}