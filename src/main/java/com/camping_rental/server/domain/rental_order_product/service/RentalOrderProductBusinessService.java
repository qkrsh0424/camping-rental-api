package com.camping_rental.server.domain.rental_order_product.service;

import com.camping_rental.server.domain.exception.dto.AccessDeniedPermissionException;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.rental_order_info.entity.RentalOrderInfoEntity;
import com.camping_rental.server.domain.rental_order_info.service.RentalOrderInfoService;
import com.camping_rental.server.domain.rental_order_info.vo.CountProductsVo;
import com.camping_rental.server.domain.rental_order_product.dto.RentalOrderProductUpdateDto;
import com.camping_rental.server.domain.rental_order_product.entity.RentalOrderProductEntity;
import com.camping_rental.server.domain.rental_order_product.enums.RentalOrderProductStatusEnum;
import com.camping_rental.server.domain.rental_order_product.projection.CountProductsProjection;
import com.camping_rental.server.domain.rental_order_product.projection.RentalOrderProductProjection;
import com.camping_rental.server.domain.rental_order_product.vo.RentalOrderProductVo;
import com.camping_rental.server.domain.room.entity.RoomEntity;
import com.camping_rental.server.domain.room.service.RoomService;
import com.camping_rental.server.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalOrderProductBusinessService {
    private final RentalOrderProductService rentalOrderProductService;
    private final RentalOrderInfoService rentalOrderInfoService;
    private final UserService userService;
    private final RoomService roomService;

    @Transactional(readOnly = true)
    public Object searchPageByPrivate(Map<String, Object> params, Pageable pageable) {
        UUID userId = userService.getUserIdOrThrow();

        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);

        params.put("roomId", roomEntity.getId());

        Page<RentalOrderProductProjection.JoinRentalOrderInfo> rentalOrderProductProjectionPage = rentalOrderProductService.qSearchPageJoinRentalOrderInfo(params, pageable);
        List<RentalOrderProductProjection.JoinRentalOrderInfo> rentalOrderProductProjections = rentalOrderProductProjectionPage.getContent();

        List<RentalOrderProductVo.JoinRentalOrderInfo> rentalOrderProductVos = rentalOrderProductProjections
                .stream().map(RentalOrderProductVo.JoinRentalOrderInfo::toVo)
                .collect(Collectors.toList());

        return new PageImpl<>(rentalOrderProductVos, pageable, rentalOrderProductProjectionPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Object searchListByRentalOrderInfoId(UUID rentalOrderInfoId) {
        UUID userId = userService.getUserIdOrThrow();

        RentalOrderInfoEntity rentalOrderInfoEntity = rentalOrderInfoService.searchByIdOrThrow(rentalOrderInfoId);

        if(!rentalOrderInfoEntity.getOrdererId().equals(userId)){
            throw new NotMatchedFormatException("해당 주문 정보에 접근 권한이 없습니다.");
        }

        List<RentalOrderProductEntity> rentalOrderProductEntities = rentalOrderProductService.searchListByRentalOrderInfoId(rentalOrderInfoId);
        List<RentalOrderProductVo.Basic> rentalOrderProductVos = rentalOrderProductEntities.stream().map(RentalOrderProductVo.Basic::toVo).collect(Collectors.toList());

        return rentalOrderProductVos;
    }

    @Transactional
    public void changeStatusToConfirmOrder(List<UUID> productIds) {
        UUID userId = userService.getUserIdOrThrow();

        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);
        List<RentalOrderProductProjection.JoinRentalOrderInfo> rentalOrderProductProjections = rentalOrderProductService.qSearchListByIdsJoinRentalOrderInfo(productIds);

        /*
        권한 체크 lenderRoomId 와 roomId 를 비교, 일치하지 않는 데이터가 있다면 throw, 권한 체크 완료 후 status 를 세팅해준다.
        Dirty checking update
         */
        rentalOrderProductProjections.forEach(r -> {
            if (!r.getRentalOrderInfoEntity().getLenderRoomId().equals(roomEntity.getId())) {
                throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
            }

            r.getRentalOrderProductEntity().setStatus(RentalOrderProductStatusEnum.CONFIRM_ORDER.getValue());
        });
    }

    @Transactional
    public void changeStatusToConfirmReservation(List<UUID> productIds) {
        UUID userId = userService.getUserIdOrThrow();

        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);
        List<RentalOrderProductProjection.JoinRentalOrderInfo> rentalOrderProductProjections = rentalOrderProductService.qSearchListByIdsJoinRentalOrderInfo(productIds);

        /*
        권한 체크 lenderRoomId 와 roomId 를 비교, 일치하지 않는 데이터가 있다면 throw, 권한 체크 완료 후 status 를 세팅해준다.
        Dirty checking update
         */
        rentalOrderProductProjections.forEach(r -> {
            if (!r.getRentalOrderInfoEntity().getLenderRoomId().equals(roomEntity.getId())) {
                throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
            }

            r.getRentalOrderProductEntity().setStatus(RentalOrderProductStatusEnum.CONFIRM_RESERVATION.getValue());
        });
    }

    @Transactional
    public void changeStatusToPickedUp(List<UUID> productIds) {
        UUID userId = userService.getUserIdOrThrow();

        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);
        List<RentalOrderProductProjection.JoinRentalOrderInfo> rentalOrderProductProjections = rentalOrderProductService.qSearchListByIdsJoinRentalOrderInfo(productIds);

        /*
        권한 체크 lenderRoomId 와 roomId 를 비교, 일치하지 않는 데이터가 있다면 throw, 권한 체크 완료 후 status 를 세팅해준다.
        Dirty checking update
         */
        rentalOrderProductProjections.forEach(r -> {
            if (!r.getRentalOrderInfoEntity().getLenderRoomId().equals(roomEntity.getId())) {
                throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
            }

            r.getRentalOrderProductEntity().setStatus(RentalOrderProductStatusEnum.PICKED_UP.getValue());
        });
    }

    @Transactional
    public void changeStatusToReturned(List<UUID> productIds) {
        UUID userId = userService.getUserIdOrThrow();

        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);
        List<RentalOrderProductProjection.JoinRentalOrderInfo> rentalOrderProductProjections = rentalOrderProductService.qSearchListByIdsJoinRentalOrderInfo(productIds);

        /*
        권한 체크 lenderRoomId 와 roomId 를 비교, 일치하지 않는 데이터가 있다면 throw, 권한 체크 완료 후 status 를 세팅해준다.
        Dirty checking update
         */
        rentalOrderProductProjections.forEach(r -> {
            if (!r.getRentalOrderInfoEntity().getLenderRoomId().equals(roomEntity.getId())) {
                throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
            }

            r.getRentalOrderProductEntity().setStatus(RentalOrderProductStatusEnum.RETURNED.getValue());
        });
    }

    @Transactional
    public void changeStatusToCompleted(List<UUID> productIds) {
        UUID userId = userService.getUserIdOrThrow();

        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);
        List<RentalOrderProductProjection.JoinRentalOrderInfo> rentalOrderProductProjections = rentalOrderProductService.qSearchListByIdsJoinRentalOrderInfo(productIds);

        /*
        권한 체크 lenderRoomId 와 roomId 를 비교, 일치하지 않는 데이터가 있다면 throw, 권한 체크 완료 후 status 를 세팅해준다.
        Dirty checking update
         */
        rentalOrderProductProjections.forEach(r -> {
            if (!r.getRentalOrderInfoEntity().getLenderRoomId().equals(roomEntity.getId())) {
                throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
            }

            r.getRentalOrderProductEntity().setStatus(RentalOrderProductStatusEnum.COMPLETED.getValue());
        });
    }

    @Transactional
    public void changeStatusToCancelled(List<UUID> productIds) {
        UUID userId = userService.getUserIdOrThrow();

        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);
        List<RentalOrderProductProjection.JoinRentalOrderInfo> rentalOrderProductProjections = rentalOrderProductService.qSearchListByIdsJoinRentalOrderInfo(productIds);

        /*
        권한 체크 lenderRoomId 와 roomId 를 비교, 일치하지 않는 데이터가 있다면 throw, 권한 체크 완료 후 status 를 세팅해준다.
        Dirty checking update
         */
        rentalOrderProductProjections.forEach(r -> {
            if (!r.getRentalOrderInfoEntity().getLenderRoomId().equals(roomEntity.getId())) {
                throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
            }

            r.getRentalOrderProductEntity().setStatus(RentalOrderProductStatusEnum.CANCELLED.getValue());
        });
    }

    @Transactional(readOnly = true)
    public Object countProducts(LocalDateTime startDate, LocalDateTime endDate, List<String> orderTypes) {
        UUID userId = userService.getUserIdOrThrow();
        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);
        UUID roomId = roomEntity.getId();

        List<CountProductsProjection.Product> countProductsProjections = rentalOrderProductService.qCountProductsByRoomId(roomId, startDate, endDate, orderTypes);
        AtomicReference<Integer> totalSum = new AtomicReference<>(0);
        Integer length = countProductsProjections.size();

        countProductsProjections.forEach(r->{
            totalSum.updateAndGet(v -> v + r.getUnitSum());
        });

        CountProductsVo.Basic countProductsVo = CountProductsVo.Basic.toVo(countProductsProjections, totalSum.get(), length);

        return countProductsVo;
    }

    @Transactional
    public Object update(RentalOrderProductUpdateDto rentalOrderProductUpdateDto) {
        UUID userId = userService.getUserIdOrThrow();
        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);
        UUID roomId = roomEntity.getId();

        if(rentalOrderProductUpdateDto.getId() == null){
            throw new NotMatchedFormatException("요청 데이터를 찾을 수 없습니다.");
        }

        RentalOrderProductProjection.JoinRentalOrderInfo rentalOrderProductProjection = rentalOrderProductService.qSearchByIdAndLenderRoomIdJoinRentalOrderInfo(rentalOrderProductUpdateDto.getId(), roomId);

        RentalOrderProductEntity rentalOrderProductEntity = rentalOrderProductProjection.getRentalOrderProductEntity();

        // Dirty checking update
        rentalOrderProductEntity.setProductName(rentalOrderProductUpdateDto.getProductName());
        rentalOrderProductEntity.setThumbnailUri(rentalOrderProductUpdateDto.getThumbnailUri());
        rentalOrderProductEntity.setPrice(rentalOrderProductUpdateDto.getPrice());
        rentalOrderProductEntity.setDiscountYn(rentalOrderProductUpdateDto.getDiscountYn());
        rentalOrderProductEntity.setDiscountMinimumHour(rentalOrderProductUpdateDto.getDiscountMinimumHour());
        rentalOrderProductEntity.setDiscountRate(rentalOrderProductUpdateDto.getDiscountRate());
        rentalOrderProductEntity.setUnit(rentalOrderProductUpdateDto.getUnit());
        rentalOrderProductEntity.setProductId(rentalOrderProductUpdateDto.getProductId());

        return RentalOrderProductVo.JoinRentalOrderInfo.toVo(rentalOrderProductProjection);
    }
}
