package com.camping_rental.server.domain.rental_order_product.service;

import com.camping_rental.server.domain.exception.dto.AccessDeniedPermissionException;
import com.camping_rental.server.domain.rental_order_product.entity.RentalOrderProductEntity;
import com.camping_rental.server.domain.rental_order_product.enums.RentalOrderProductStatusEnum;
import com.camping_rental.server.domain.rental_order_product.projection.RentalOrderProductProjection;
import com.camping_rental.server.domain.rental_order_product.vo.RentalOrderProductVo;
import com.camping_rental.server.domain.room.entity.RoomEntity;
import com.camping_rental.server.domain.room.service.RoomSerivce;
import com.camping_rental.server.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalOrderProductBusinessService {
    private final RentalOrderProductService rentalOrderProductService;
    private final UserService userService;
    private final RoomSerivce roomSerivce;

    public Object searchPageByPrivate(Map<String, Object> params, Pageable pageable) {
        UUID userId = userService.getUserIdOrThrow();

        RoomEntity roomEntity = roomSerivce.searchByUserIdOrThrow(userId);

        params.put("roomId", roomEntity.getId());

        Page<RentalOrderProductProjection.JoinRentalOrderInfo> rentalOrderProductProjectionPage = rentalOrderProductService.qSearchPageJoinRentalOrderInfo(params, pageable);
        List<RentalOrderProductProjection.JoinRentalOrderInfo> rentalOrderProductProjections = rentalOrderProductProjectionPage.getContent();

        List<RentalOrderProductVo.JoinRentalOrderInfo> rentalOrderProductVos = rentalOrderProductProjections
                .stream().map(RentalOrderProductVo.JoinRentalOrderInfo::toVo)
                .collect(Collectors.toList());

        return new PageImpl<>(rentalOrderProductVos, pageable, rentalOrderProductProjectionPage.getTotalElements());
    }

    @Transactional
    public void changeStatusToConfirmOrder(List<UUID> productIds) {
        UUID userId = userService.getUserIdOrThrow();

        RoomEntity roomEntity = roomSerivce.searchByUserIdOrThrow(userId);
        List<RentalOrderProductProjection.JoinRentalOrderInfo> rentalOrderProductProjections = rentalOrderProductService.qSearchListByIdsJoinRentalOrderInfo(productIds);

        /*
        ?????? ?????? lenderRoomId ??? roomId ??? ??????, ???????????? ?????? ???????????? ????????? throw, ?????? ?????? ?????? ??? status ??? ???????????????.
        Dirty checking update
         */
        rentalOrderProductProjections.forEach(r->{
            if(!r.getRentalOrderInfoEntity().getLenderRoomId().equals(roomEntity.getId())){
                throw new AccessDeniedPermissionException("?????? ????????? ????????????.");
            }

            r.getRentalOrderProductEntity().setStatus(RentalOrderProductStatusEnum.CONFIRM_ORDER.getValue());
        });
    }

    @Transactional
    public void changeStatusToConfirmReservation(List<UUID> productIds) {
        UUID userId = userService.getUserIdOrThrow();

        RoomEntity roomEntity = roomSerivce.searchByUserIdOrThrow(userId);
        List<RentalOrderProductProjection.JoinRentalOrderInfo> rentalOrderProductProjections = rentalOrderProductService.qSearchListByIdsJoinRentalOrderInfo(productIds);

        /*
        ?????? ?????? lenderRoomId ??? roomId ??? ??????, ???????????? ?????? ???????????? ????????? throw, ?????? ?????? ?????? ??? status ??? ???????????????.
        Dirty checking update
         */
        rentalOrderProductProjections.forEach(r->{
            if(!r.getRentalOrderInfoEntity().getLenderRoomId().equals(roomEntity.getId())){
                throw new AccessDeniedPermissionException("?????? ????????? ????????????.");
            }

            r.getRentalOrderProductEntity().setStatus(RentalOrderProductStatusEnum.CONFIRM_RESERVATION.getValue());
        });
    }

    @Transactional
    public void changeStatusToPickedUp(List<UUID> productIds) {
        UUID userId = userService.getUserIdOrThrow();

        RoomEntity roomEntity = roomSerivce.searchByUserIdOrThrow(userId);
        List<RentalOrderProductProjection.JoinRentalOrderInfo> rentalOrderProductProjections = rentalOrderProductService.qSearchListByIdsJoinRentalOrderInfo(productIds);

        /*
        ?????? ?????? lenderRoomId ??? roomId ??? ??????, ???????????? ?????? ???????????? ????????? throw, ?????? ?????? ?????? ??? status ??? ???????????????.
        Dirty checking update
         */
        rentalOrderProductProjections.forEach(r->{
            if(!r.getRentalOrderInfoEntity().getLenderRoomId().equals(roomEntity.getId())){
                throw new AccessDeniedPermissionException("?????? ????????? ????????????.");
            }

            r.getRentalOrderProductEntity().setStatus(RentalOrderProductStatusEnum.PICKED_UP.getValue());
        });
    }

    @Transactional
    public void changeStatusToReturned(List<UUID> productIds) {
        UUID userId = userService.getUserIdOrThrow();

        RoomEntity roomEntity = roomSerivce.searchByUserIdOrThrow(userId);
        List<RentalOrderProductProjection.JoinRentalOrderInfo> rentalOrderProductProjections = rentalOrderProductService.qSearchListByIdsJoinRentalOrderInfo(productIds);

        /*
        ?????? ?????? lenderRoomId ??? roomId ??? ??????, ???????????? ?????? ???????????? ????????? throw, ?????? ?????? ?????? ??? status ??? ???????????????.
        Dirty checking update
         */
        rentalOrderProductProjections.forEach(r->{
            if(!r.getRentalOrderInfoEntity().getLenderRoomId().equals(roomEntity.getId())){
                throw new AccessDeniedPermissionException("?????? ????????? ????????????.");
            }

            r.getRentalOrderProductEntity().setStatus(RentalOrderProductStatusEnum.RETURNED.getValue());
        });
    }

    @Transactional
    public void changeStatusToCompleted(List<UUID> productIds) {
        UUID userId = userService.getUserIdOrThrow();

        RoomEntity roomEntity = roomSerivce.searchByUserIdOrThrow(userId);
        List<RentalOrderProductProjection.JoinRentalOrderInfo> rentalOrderProductProjections = rentalOrderProductService.qSearchListByIdsJoinRentalOrderInfo(productIds);

        /*
        ?????? ?????? lenderRoomId ??? roomId ??? ??????, ???????????? ?????? ???????????? ????????? throw, ?????? ?????? ?????? ??? status ??? ???????????????.
        Dirty checking update
         */
        rentalOrderProductProjections.forEach(r->{
            if(!r.getRentalOrderInfoEntity().getLenderRoomId().equals(roomEntity.getId())){
                throw new AccessDeniedPermissionException("?????? ????????? ????????????.");
            }

            r.getRentalOrderProductEntity().setStatus(RentalOrderProductStatusEnum.COMPLETED.getValue());
        });
    }

    @Transactional
    public void changeStatusToCancelled(List<UUID> productIds) {
        UUID userId = userService.getUserIdOrThrow();

        RoomEntity roomEntity = roomSerivce.searchByUserIdOrThrow(userId);
        List<RentalOrderProductProjection.JoinRentalOrderInfo> rentalOrderProductProjections = rentalOrderProductService.qSearchListByIdsJoinRentalOrderInfo(productIds);

        /*
        ?????? ?????? lenderRoomId ??? roomId ??? ??????, ???????????? ?????? ???????????? ????????? throw, ?????? ?????? ?????? ??? status ??? ???????????????.
        Dirty checking update
         */
        rentalOrderProductProjections.forEach(r->{
            if(!r.getRentalOrderInfoEntity().getLenderRoomId().equals(roomEntity.getId())){
                throw new AccessDeniedPermissionException("?????? ????????? ????????????.");
            }

            r.getRentalOrderProductEntity().setStatus(RentalOrderProductStatusEnum.CANCELLED.getValue());
        });
    }
}
