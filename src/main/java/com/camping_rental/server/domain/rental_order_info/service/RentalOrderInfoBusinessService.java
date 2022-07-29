package com.camping_rental.server.domain.rental_order_info.service;

import com.camping_rental.server.domain.exception.dto.AccessDeniedPermissionException;
import com.camping_rental.server.domain.product.entity.ProductEntity;
import com.camping_rental.server.domain.product.projection.ProductProjection;
import com.camping_rental.server.domain.product.service.ProductService;
import com.camping_rental.server.domain.rental_order_info.dto.RentalOrderInfoDto;
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
import com.camping_rental.server.domain.twilio.dto.TwilioSmsRequestDto;
import com.camping_rental.server.domain.twilio.service.TwilioSmsService;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.utils.CustomDateUtils;
import com.camping_rental.server.utils.CustomUniqueKeyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalOrderInfoBusinessService {
    private final ProductService productService;
    private final UserService userService;
    private final RentalOrderInfoService rentalOrderInfoService;
    private final RentalOrderProductService rentalOrderProductService;
    private final TwilioSmsService twilioSmsService;
    private final RoomService roomService;

    @Transactional
    public Map<String, Object> createOne(RentalOrderInfoDto.Create rentalOrderInfoDto) {
        /*
        필드값 validation check
         */
        RentalOrderInfoDto.Create.checkFieldFormatValid(rentalOrderInfoDto);

        /*
        주문자 아이디 가져오기
         */
        UUID userId = userService.getUserIdOrNull();

        List<RentalOrderProductDto.Create> rentalOrderProductDtos = rentalOrderInfoDto.getRentalOrderProducts();
        List<UUID> productIds = rentalOrderProductDtos.stream().map(r -> r.getProductId()).collect(Collectors.toList());

        /*
        상품 데이터 가져오기
         */
        List<ProductProjection.JoinRoomAndRegions> productProjections = productService.qSearchListByIdsJoinRoomAndRegions(productIds);

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
                .pickupDate(rentalOrderInfoDto.getPickupDate())
                .pickupTime(rentalOrderInfoDto.getPickupTime())
                .pickupPlace(rentalOrderInfoDto.getPickupPlace())
                .returnDate(rentalOrderInfoDto.getReturnDate())
                .returnTime(rentalOrderInfoDto.getReturnTime())
                .returnPlace(rentalOrderInfoDto.getReturnPlace())
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
        twilio 메세지 전송
         */
        List<TwilioSmsRequestDto> twilioSmsRequestDtos = new ArrayList<>();

        StringBuilder ordererSmsMessage = new StringBuilder();
        ordererSmsMessage.append("[Campal | 캠핑 렌탈]\n")
                .append("주문이 정상적으로 접수 되었습니다.\n\n")
                .append("주문번호: " + orderNumber + "\n\n")
                .append("주문 내역 조회 바로가기: http://www.campal.co.kr/search/order")
        ;

        StringBuilder lenderSmsMessage = new StringBuilder();
        lenderSmsMessage.append("[Campal | 캠핑 렌탈]\n\n")
                .append("신규 주문이 있습니다.\n\n")
                .append("주문자 성함 : " + rentalOrderInfoDto.getOrderer() + "\n")
                .append("주문자 전화번호 : " + rentalOrderInfoDto.getOrdererPhoneNumber() + "\n\n")
                .append("조회 링크 바로가기 : " + "http://www.campal.co.kr/myadmin")
        ;

        StringBuilder adminSmsMessage = new StringBuilder();
        adminSmsMessage.append("[Campal | 캠핑 렌탈] for Admin\n\n")
                .append(lenderRoomEntity.getName() + " 님에게 신규 주문이 있습니다.\n\n")
                .append("주문자 성함 : " + rentalOrderInfoDto.getOrderer() + "\n")
                .append("주문자 전화번호 : " + rentalOrderInfoDto.getOrdererPhoneNumber() + "\n\n")
        ;

//        TODO SETTING : 운영시 주석 풀어줘야함.
        twilioSmsRequestDtos.add(
                TwilioSmsRequestDto.toDto(
                        rentalOrderInfoDto.getOrdererPhoneNumber(),
                        ordererSmsMessage.toString()
                )
        );
        twilioSmsRequestDtos.add(
                TwilioSmsRequestDto.toDto(
                        lenderRoomPhoneNumber,
                        lenderSmsMessage.toString()
                )
        );
        twilioSmsRequestDtos.add(
                TwilioSmsRequestDto.toDto(
                        "01085356112",
                        adminSmsMessage.toString()
                )
        );
//        twilioSmsRequestDtos.add(
//                TwilioSmsRequestDto.toDto(
//                        "01050036206",
//                        lenderSmsMessage.toString()
//                )
//        );
//        twilioSmsRequestDtos.add(
//                TwilioSmsRequestDto.toDto(
//                        "01063760015",
//                        lenderSmsMessage.toString()
//                )
//        );
        twilioSmsService.sendMultipleSms(twilioSmsRequestDtos);

        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("orderNumber", orderNumber);
        resultMap.put("orderer", rentalOrderInfoEntity.getOrderer());
        resultMap.put("ordererPhoneNumber", rentalOrderInfoEntity.getOrdererPhoneNumber());
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

    public void sendSms(UUID rentalOrderInfoId, String smsMessage) {
        UUID userId = userService.getUserIdOrThrow();
        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);

        RentalOrderInfoEntity rentalOrderInfoEntity = rentalOrderInfoService.searchByIdOrThrow(rentalOrderInfoId);

        if (!rentalOrderInfoEntity.getLenderRoomId().equals(roomEntity.getId())) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        List<TwilioSmsRequestDto> twilioSmsRequestDtos = new ArrayList<>();

        twilioSmsRequestDtos.add(
                TwilioSmsRequestDto.toDto(
                        rentalOrderInfoEntity.getOrdererPhoneNumber(),
                        smsMessage
                )
        );

        twilioSmsService.sendMultipleSms(twilioSmsRequestDtos);
    }
}
