package com.camping_rental.server.domain.rental_order_info.service;

import com.camping_rental.server.domain.exception.dto.AccessDeniedPermissionException;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.naver.sens.dto.NaverCloudSensSmsSendDto;
import com.camping_rental.server.domain.naver.sens.service.NaverCloudSensSmsService;
import com.camping_rental.server.domain.naver.sens.strategy.NaverCloudSensSmsRentalOrderInfo;
import com.camping_rental.server.domain.naver.sens.strategy.NaverCloudSensSmsRequestFactory;
import com.camping_rental.server.domain.product.entity.ProductEntity;
import com.camping_rental.server.domain.product.projection.ProductProjection;
import com.camping_rental.server.domain.product.service.ProductService;
import com.camping_rental.server.domain.rental_order_info.dto.RentalOrderInfoCreateUtils;
import com.camping_rental.server.domain.rental_order_info.dto.RentalOrderInfoDto;
import com.camping_rental.server.domain.rental_order_info.dto.RentalOrderInfoUpdateDto;
import com.camping_rental.server.domain.rental_order_info.entity.RentalOrderInfoEntity;
import com.camping_rental.server.domain.rental_order_info.enums.RentalOrderInfoDeletedFlagEnum;
import com.camping_rental.server.domain.rental_order_info.enums.RentalOrderInfoOrdererTypeEnum;
import com.camping_rental.server.domain.rental_order_info.projection.RentalOrderInfoProjection;
import com.camping_rental.server.domain.rental_order_info.vo.RentalOrderInfoVo;
import com.camping_rental.server.domain.rental_order_product.dto.RentalOrderProductDto;
import com.camping_rental.server.domain.rental_order_product.entity.RentalOrderProductEntity;
import com.camping_rental.server.domain.rental_order_product.enums.RentalOrderProductDeletedFlagEnum;
import com.camping_rental.server.domain.rental_order_product.enums.RentalOrderProductStatusEnum;
import com.camping_rental.server.domain.rental_order_product.service.RentalOrderProductService;
import com.camping_rental.server.domain.room.entity.RoomEntity;
import com.camping_rental.server.domain.room.service.RoomService;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.utils.CustomDateUtils;
import com.camping_rental.server.utils.CustomUniqueKeyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalOrderInfoBusinessService {
    private final ProductService productService;
    private final UserService userService;
    private final RentalOrderInfoService rentalOrderInfoService;
    private final RentalOrderProductService rentalOrderProductService;
    private final RoomService roomService;
    private final NaverCloudSensSmsService naverCloudSensSmsService;

    @Transactional
    public Map<String, Object> createOne(HttpServletRequest request, RentalOrderInfoDto.Create rentalOrderInfoDto) {
        /*
        == Fields Form Validation Check Start
         */
        RentalOrderInfoCreateUtils.checkServiceAgreementYnValid(rentalOrderInfoDto.getServiceAgreementYn());
        RentalOrderInfoCreateUtils.checkOrdererValid(rentalOrderInfoDto.getOrderer());
        RentalOrderInfoCreateUtils.checkOrdererPhoneNumberValid(rentalOrderInfoDto.getOrdererPhoneNumber());
        RentalOrderInfoCreateUtils.checkPickupPlaceValid(rentalOrderInfoDto.getPickupPlace());
        RentalOrderInfoCreateUtils.checkReturnPlaceValid(rentalOrderInfoDto.getReturnPlace());
        RentalOrderInfoCreateUtils.checkPickupDateValid(rentalOrderInfoDto.getPickupDate());
        RentalOrderInfoCreateUtils.checkReturnDateValid(rentalOrderInfoDto.getReturnDate());
        RentalOrderInfoCreateUtils.checkPickupTimeValid(rentalOrderInfoDto.getPickupTime());
        RentalOrderInfoCreateUtils.checkReturnTimeValid(rentalOrderInfoDto.getReturnTime());

        if (!rentalOrderInfoDto.isSameWithOrdererFlag()) {
            RentalOrderInfoCreateUtils.checkBorrowerValid(rentalOrderInfoDto.getBorrower());
            RentalOrderInfoCreateUtils.checkBorrowerPhoneNumberValid(rentalOrderInfoDto.getBorrowerPhoneNumber());
            RentalOrderInfoCreateUtils.checkBorrowerPhoneNumberValidationCodeValid(rentalOrderInfoDto.getBorrowerPhoneNumberValidationCode());
            RentalOrderInfoCreateUtils.checkPhoneValidationValid(request, rentalOrderInfoDto.getBorrowerPhoneNumber(), rentalOrderInfoDto.getBorrowerPhoneNumberValidationCode());
        }
        /*
        == Fields Form Validation Check End
         */

        /*
        주문자 아이디 가져오기
         */
        UUID userId = userService.getUserIdOrNull();

        List<RentalOrderProductDto.Create> rentalOrderProductDtos = rentalOrderInfoDto.getRentalOrderProducts();
        List<UUID> productIds = rentalOrderProductDtos.stream().map(r -> r.getProductId()).collect(Collectors.toList());

        /*
        상품 데이터 가져오기
         */
        List<ProductProjection.RelatedRoomAndRegions> productProjections = productService.qSearchListByIdsRelatedRoomAndRegions(productIds);

        /*
        상품 데이터중 첫번째 데이터의 room 을 가져와서 lenderRoomId로 지정한다.
        어차피 room 데이터는 모든 상품에 동일하다.
         */
        RoomEntity lenderRoomEntity = productProjections.stream().findFirst().get().getRoomEntity();
        UUID lenderRoomId = lenderRoomEntity.getId();
        String lenderRoomPhoneNumber = lenderRoomEntity.getPhoneNumber();

        UUID rentalOrderInfoId = UUID.randomUUID();
        String orderNumber = CustomUniqueKeyUtils.generateOrderNumber18();

        RentalOrderInfoEntity rentalOrderInfoEntity = RentalOrderInfoEntity.builder()
                .cid(null)
                .id(rentalOrderInfoId)
                .orderNumber(orderNumber)
                .orderer(rentalOrderInfoDto.getOrderer())
                .ordererPhoneNumber(rentalOrderInfoDto.getOrdererPhoneNumber())
                .borrower(rentalOrderInfoDto.getBorrower())
                .borrowerPhoneNumber(rentalOrderInfoDto.getBorrowerPhoneNumber())
                .pickupDate(rentalOrderInfoDto.getPickupDate())
                .pickupTime(rentalOrderInfoDto.getPickupTime())
                .pickupPlace(rentalOrderInfoDto.getPickupPlace())
                .returnDate(rentalOrderInfoDto.getReturnDate())
                .returnTime(rentalOrderInfoDto.getReturnTime())
                .returnPlace(rentalOrderInfoDto.getReturnPlace())
                .csMemo("")
                .serviceAgreementYn(rentalOrderInfoDto.getServiceAgreementYn())
                .createdAt(CustomDateUtils.getCurrentDateTime())
                .updatedAt(CustomDateUtils.getCurrentDateTime())
                .deletedFlag(RentalOrderInfoDeletedFlagEnum.EXIST.getValue())
                .ordererType(userId != null ? RentalOrderInfoOrdererTypeEnum.MEMBER.getValue() : RentalOrderInfoOrdererTypeEnum.NON_MEMBER.getValue())
                .ordererId(userId)
                .lenderRoomId(lenderRoomId)
                .build();

        List<RentalOrderProductEntity> rentalOrderProductEntities = rentalOrderProductDtos.stream().map(rentalOrderProductDto -> {
            ProductEntity productEntity = productProjections.stream().filter(r -> r.getProductEntity().getId().equals(rentalOrderProductDto.getProductId())).findFirst().get().getProductEntity();
            RentalOrderProductEntity rentalOrderProductEntity = RentalOrderProductEntity.builder()
                    .cid(null)
                    .id(UUID.randomUUID())
                    .status(RentalOrderProductStatusEnum.NEW_ORDER.getValue())
                    .productName(productEntity.getName())
                    .thumbnailUri(productEntity.getThumbnailUri())
                    .price(productEntity.getPrice())
                    .discountYn(productEntity.getDiscountYn())
                    .discountMinimumHour(productEntity.getDiscountMinimumHour())
                    .discountRate(productEntity.getDiscountRate())
                    .unit(rentalOrderProductDto.getUnit())
                    .deletedFlag(RentalOrderProductDeletedFlagEnum.EXIST.getValue())
                    .rentalOrderInfoId(rentalOrderInfoId)
                    .productId(productEntity.getId())
                    .build();
            return rentalOrderProductEntity;
        }).collect(Collectors.toList());

        rentalOrderInfoService.saveAndModify(rentalOrderInfoEntity);
        rentalOrderProductService.saveAll(rentalOrderProductEntities);

        /*
        SENS 메세지 전송
         */
        List<NaverCloudSensSmsSendDto> naverCloudSensSmsSendDtoList = new ArrayList<>();
        NaverCloudSensSmsRequestFactory naverCloudSensSmsRequestFactory = new NaverCloudSensSmsRequestFactory();

        naverCloudSensSmsRequestFactory.setNaverCloudSensSmsRequestStrategy(new NaverCloudSensSmsRentalOrderInfo.Orderer());
        NaverCloudSensSmsSendDto ordererSms = naverCloudSensSmsRequestFactory.make(Map.of(
                "orderNumber", orderNumber,
                "smsReceiverPhoneNumber", rentalOrderInfoDto.getOrdererPhoneNumber(),
                "borrower", rentalOrderInfoDto.getBorrower(),
                "borrowerPhoneNumber", rentalOrderInfoDto.getBorrowerPhoneNumber()
        ));

        naverCloudSensSmsRequestFactory.setNaverCloudSensSmsRequestStrategy(new NaverCloudSensSmsRentalOrderInfo.Lender());
        NaverCloudSensSmsSendDto lenderSms = naverCloudSensSmsRequestFactory.make(Map.of(
                "smsReceiverPhoneNumber", lenderRoomPhoneNumber,
                "borrower", rentalOrderInfoDto.getBorrower(),
                "borrowerPhoneNumber", rentalOrderInfoDto.getBorrowerPhoneNumber()
        ));

        naverCloudSensSmsRequestFactory.setNaverCloudSensSmsRequestStrategy(new NaverCloudSensSmsRentalOrderInfo.Admin());
        NaverCloudSensSmsSendDto adminSms = naverCloudSensSmsRequestFactory.make(Map.of(
                "smsReceiverPhoneNumber", "01085356112",
                "lender", lenderRoomEntity.getName(),
                "orderer", rentalOrderInfoDto.getOrderer(),
                "ordererPhoneNumber", rentalOrderInfoDto.getOrdererPhoneNumber(),
                "borrower", rentalOrderInfoDto.getBorrower(),
                "borrowerPhoneNumber", rentalOrderInfoDto.getBorrowerPhoneNumber()
        ));

        naverCloudSensSmsSendDtoList.add(ordererSms);
        naverCloudSensSmsSendDtoList.add(lenderSms);
        naverCloudSensSmsSendDtoList.add(adminSms);

        naverCloudSensSmsService.sendMultiple(naverCloudSensSmsSendDtoList);

        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("orderNumber", orderNumber);
        resultMap.put("borrower", rentalOrderInfoEntity.getBorrower());
        resultMap.put("borrowerPhoneNumber", rentalOrderInfoEntity.getBorrowerPhoneNumber());
        resultMap.put("lender", lenderRoomEntity.getName());
        resultMap.put("lenderPhoneNumber", lenderRoomPhoneNumber);

        return resultMap;
    }

    public Object searchOneByOrderNumberAndOrdererAndOrderPhoneNumber(String orderNumber, String orderer, String ordererPhoneNumber) {
        RentalOrderInfoProjection.FullJoin rentalOrderInfoProjection = rentalOrderInfoService.qSearchOneFullJoinByOrderNumberAndOrdererAndOrdererPhoneNumber(orderNumber, orderer, ordererPhoneNumber);

        RentalOrderInfoVo.FullJoin vo = RentalOrderInfoVo.FullJoin.toVo(rentalOrderInfoProjection);
        return vo;
    }

    public Object searchPage(Pageable pageable) {
        UUID userId = userService.getUserIdOrThrow();

        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);

        Page<RentalOrderInfoProjection.FullJoin> rentalOrderInfoProjectionPage = rentalOrderInfoService.qSearchPageFullJoinByRoomId(roomEntity.getId(), pageable);
        List<RentalOrderInfoVo.FullJoin> vos = rentalOrderInfoProjectionPage.getContent().stream().map(r -> {
            return RentalOrderInfoVo.FullJoin.toVo(r);
        }).collect(Collectors.toList());

        return new PageImpl<>(vos, pageable, rentalOrderInfoProjectionPage.getTotalElements());
    }

    public Object searchPageMyOrder(Pageable pageable) {
        UUID userId = userService.getUserIdOrThrow();

        Page<RentalOrderInfoProjection.RelatedRoom> rentalOrderInfoProjectionPage = rentalOrderInfoService.qSearchPageByUserIdRelatedRoom(userId, pageable);
        List<RentalOrderInfoVo.RelatedRoom> vos = rentalOrderInfoProjectionPage.getContent().stream().map(r -> {
            return RentalOrderInfoVo.RelatedRoom.toVo(r);
        }).collect(Collectors.toList());

        return new PageImpl<>(vos, pageable, rentalOrderInfoProjectionPage.getTotalElements());
    }

    @Transactional
    public void changeCsMemo(UUID rentalOrderInfoId, String csMemo) {
        UUID userId = userService.getUserIdOrThrow();
        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);

        RentalOrderInfoEntity rentalOrderInfoEntity = rentalOrderInfoService.searchByIdOrThrow(rentalOrderInfoId);

        if (!rentalOrderInfoEntity.getLenderRoomId().equals(roomEntity.getId())) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        /*
        Dirty checking update
         */
        rentalOrderInfoEntity.setCsMemo(csMemo);
    }

    @Transactional
    public RentalOrderInfoVo.Basic update(RentalOrderInfoUpdateDto rentalOrderInfoUpdateDto) {
        UUID userId = userService.getUserIdOrThrow();
        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);
        UUID rentalOrderInfoId = null;

        try {
            rentalOrderInfoId = UUID.fromString(rentalOrderInfoUpdateDto.getId());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다.");
        }

        RentalOrderInfoEntity rentalOrderInfoEntity = rentalOrderInfoService.searchByIdOrThrow(rentalOrderInfoId);

        if (!rentalOrderInfoEntity.getLenderRoomId().equals(roomEntity.getId())) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        /*
        Dirty checking update
         */
        rentalOrderInfoEntity.setBorrower(rentalOrderInfoUpdateDto.getBorrower());
        rentalOrderInfoEntity.setBorrowerPhoneNumber(rentalOrderInfoUpdateDto.getBorrowerPhoneNumber());
        rentalOrderInfoEntity.setPickupDate(rentalOrderInfoUpdateDto.getPickupDate());
        rentalOrderInfoEntity.setPickupTime(rentalOrderInfoUpdateDto.getPickupTime());
        rentalOrderInfoEntity.setPickupPlace(rentalOrderInfoUpdateDto.getPickupPlace());
        rentalOrderInfoEntity.setReturnDate(rentalOrderInfoUpdateDto.getReturnDate());
        rentalOrderInfoEntity.setReturnTime(rentalOrderInfoUpdateDto.getReturnTime());
        rentalOrderInfoEntity.setReturnPlace(rentalOrderInfoUpdateDto.getReturnPlace());
        rentalOrderInfoEntity.setCsMemo(rentalOrderInfoUpdateDto.getCsMemo());
        rentalOrderInfoEntity.setUpdatedAt(CustomDateUtils.getCurrentDateTime());

        RentalOrderInfoVo.Basic vo =RentalOrderInfoVo.Basic.toVo(rentalOrderInfoEntity);

        return vo;
    }
}
