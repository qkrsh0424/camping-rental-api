package com.camping_rental.server.domain.rental_order_info.service;

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
import com.camping_rental.server.domain.twilio.dto.TwilioSmsRequestDto;
import com.camping_rental.server.domain.twilio.service.TwilioSmsService;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.utils.CustomDateUtils;
import com.camping_rental.server.utils.CustomUniqueKeyUtils;
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
        ????????? validation check
         */
        RentalOrderInfoDto.Create.checkFieldFormatValid(rentalOrderInfoDto);

        /*
        ????????? ????????? ????????????
         */
        UUID userId = userService.getUserIdOrNull();

        List<RentalOrderProductDto.Create> rentalOrderProductDtos = rentalOrderInfoDto.getRentalOrderProducts();
        List<UUID> productIds = rentalOrderProductDtos.stream().map(r -> r.getProductId()).collect(Collectors.toList());

        /*
        ?????? ????????? ????????????
         */
        List<ProductProjection.JoinRoomAndRegions> productProjections = productService.qSearchListByIdsJoinRoomAndRegions(productIds);

        /*
        ?????? ???????????? ????????? ???????????? room ??? ???????????? lenderRoomId??? ????????????.
        ????????? room ???????????? ?????? ????????? ????????????.
         */
        UUID lenderRoomId = productProjections.stream().findFirst().get().getRoomEntity().getId();

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
        twilio ????????? ??????
         */
        List<TwilioSmsRequestDto> twilioSmsRequestDtos = new ArrayList<>();

        StringBuilder ordererSmsMessage = new StringBuilder();
        ordererSmsMessage.append("[Campal | ?????? ??????]\n")
                .append("????????? ??????????????? ?????? ???????????????.\n\n")
                .append("????????????: " + orderNumber + "\n\n")
                .append("?????? ?????? ?????? ????????????: http://www.campal.co.kr/search/order")
        ;


        StringBuilder lenderSmsMessage = new StringBuilder();
        lenderSmsMessage.append("[Campal | ?????? ??????]\n\n");
        lenderSmsMessage.append("?????? ????????? ????????????.\n\n");
        lenderSmsMessage.append("????????? ?????? : " + rentalOrderInfoDto.getOrderer() + "\n");
        lenderSmsMessage.append("????????? ???????????? : " + rentalOrderInfoDto.getOrdererPhoneNumber() + "\n\n");
        lenderSmsMessage.append("?????? ?????? ???????????? : " + "http://www.campal.co.kr/myadmin");

//        TODO SETTING : ????????? ?????? ???????????????.
        twilioSmsRequestDtos.add(
                TwilioSmsRequestDto.toDto(
                        rentalOrderInfoDto.getOrdererPhoneNumber(),
                        ordererSmsMessage.toString()
                )
        );
//        twilioSmsRequestDtos.add(
//                TwilioSmsRequestDto.toDto(
//                        "01085356112",
//                        lenderSmsMessage.toString()
//                )
//        );
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
    }

    public Object searchOneByOrderNumberAndOrdererAndOrderPhoneNumber(String orderNumber, String orderer, String ordererPhoneNumber) {
        RentalOrderInfoProjection.FullJoin rentalOrderInfoProjection = rentalOrderInfoService.qSearchOneFullJoinByOrderNumberAndOrdererAndOrdererPhoneNumber(orderNumber, orderer, ordererPhoneNumber);

        RentalOrderInfoVo.FullJoin vo = RentalOrderInfoVo.FullJoin.toVo(rentalOrderInfoProjection);
        return vo;
    }
}
