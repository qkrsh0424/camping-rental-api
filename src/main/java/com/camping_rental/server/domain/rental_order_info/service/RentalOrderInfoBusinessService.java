package com.camping_rental.server.domain.rental_order_info.service;

import com.camping_rental.server.domain.product.entity.ProductEntity;
import com.camping_rental.server.domain.product.projection.ProductProjection;
import com.camping_rental.server.domain.product.service.ProductService;
import com.camping_rental.server.domain.rental_order_info.dto.RentalOrderInfoDto;
import com.camping_rental.server.domain.rental_order_info.entity.RentalOrderInfoEntity;
import com.camping_rental.server.domain.rental_order_info.enums.RentalOrderInfoDeletedFlagEnum;
import com.camping_rental.server.domain.rental_order_info.enums.RentalOrderInfoOrdererTypeEnum;
import com.camping_rental.server.domain.rental_order_product.dto.RentalOrderProductDto;
import com.camping_rental.server.domain.rental_order_product.entity.RentalOrderProductEntity;
import com.camping_rental.server.domain.rental_order_product.enums.RentalOrderProductDeletedFlagEnum;
import com.camping_rental.server.domain.rental_order_product.enums.RentalOrderProductStatusEnum;
import com.camping_rental.server.domain.rental_order_product.service.RentalOrderProductService;
import com.camping_rental.server.domain.twilio.dto.TwilioSmsRequestDto;
import com.camping_rental.server.domain.twilio.service.TwilioSmsService;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.utils.CustomDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalOrderInfoBusinessService {
    private final ProductService productService;
    private final UserService userService;
    private final RentalOrderInfoService rentalOrderInfoService;
    private final RentalOrderProductService rentalOrderProductService;
    private final TwilioSmsService twilioSmsService;

    @Transactional
    public void createOne(RentalOrderInfoDto.Create rentalOrderInfoDto) {
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
        UUID lenderRoomId = productProjections.stream().findFirst().get().getRoomEntity().getId();

        UUID rentalOrderInfoId = UUID.randomUUID();
        RentalOrderInfoEntity rentalOrderInfoEntity = RentalOrderInfoEntity.builder()
                .cid(null)
                .id(rentalOrderInfoId)
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
            ProductEntity productEntity = productProjections.stream().filter(r->r.getProductEntity().getId().equals(rentalOrderProductDto.getProductId())).findFirst().get().getProductEntity();
            RentalOrderProductEntity rentalOrderProductEntity = RentalOrderProductEntity.builder()
                    .cid(null)
                    .id(UUID.randomUUID())
                    .status(RentalOrderProductStatusEnum.NEW_ORDER.getValue())
                    .productName(productEntity.getName())
                    .thumbnailUri(productEntity.getThumbnailUri())
                    .price(productEntity.getPrice())
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

        StringBuilder smsMessage = new StringBuilder();
        smsMessage.append("[Campal | 캠핑 렌탈]\n\n");
        smsMessage.append("신규 주문이 있습니다.\n\n");
        smsMessage.append("주문자 성함 : " + rentalOrderInfoDto.getOrderer() + "\n");
        smsMessage.append("주문자 전화번호 : " + rentalOrderInfoDto.getOrdererPhoneNumber() + "\n\n");
        smsMessage.append("조회 링크 바로가기 : " + "http://www.campal.co.kr/myadmin");

//        TODO SETTING : 운영시 주석 풀어줘야함.
        twilioSmsRequestDtos.add(
                TwilioSmsRequestDto.toDto(
                        "01085356112",
                        smsMessage.toString()
                )
        );
        twilioSmsRequestDtos.add(
                TwilioSmsRequestDto.toDto(
                        "01050036206",
                        smsMessage.toString()
                )
        );
        twilioSmsRequestDtos.add(
                TwilioSmsRequestDto.toDto(
                        "01063760015",
                        smsMessage.toString()
                )
        );
        twilioSmsService.sendMultipleSms(twilioSmsRequestDtos);
    }
}
