package com.camping_rental.server.domain.order.service;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.order.vo.OrderVo;
import com.camping_rental.server.domain.order_info.dto.OrderInfoDto;
import com.camping_rental.server.domain.order_info.entity.OrderInfoEntity;
import com.camping_rental.server.domain.order_info.service.OrderInfoService;
import com.camping_rental.server.domain.order_info.vo.OrderInfoVo;
import com.camping_rental.server.domain.order_item.dto.OrderItemDto;
import com.camping_rental.server.domain.order_item.entity.OrderItemEntity;
import com.camping_rental.server.domain.order_item.service.OrderItemService;
import com.camping_rental.server.domain.order_item.vo.OrderItemVo;
import com.camping_rental.server.domain.twilio.dto.TwilioSmsRequestDto;
import com.camping_rental.server.domain.twilio.service.TwilioSmsService;
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
public class OrderBusinessService {
    private final OrderInfoService orderInfoService;
    private final OrderItemService orderItemService;
    private final TwilioSmsService twilioSmsService;

    @Transactional
    public void create(OrderInfoDto orderInfoDto, List<OrderItemDto> orderItemDtos) {
        if (!orderInfoDto.getServiceAgreementYn().equals("y")) {
            throw new NotMatchedFormatException("개인정보수집 및 이용에 동의해 주세요.");
        }
        UUID ORDER_INFO_ID = UUID.randomUUID();

        orderInfoDto.setId(ORDER_INFO_ID);
        orderInfoDto.setCreatedAt(CustomDateUtils.getCurrentDateTime());

        OrderInfoEntity orderInfoEntity = OrderInfoEntity.toEntity(orderInfoDto);
        List<OrderItemEntity> orderItemEntities = orderItemDtos.stream().map(r -> {
            UUID ORDER_ITEM_ID = UUID.randomUUID();
            r.setId(ORDER_ITEM_ID);
            r.setOrderInfoId(ORDER_INFO_ID);
            return OrderItemEntity.toEntity(r);
        }).collect(Collectors.toList());

        orderInfoService.saveAndModify(orderInfoEntity);
        orderItemService.saveAll(orderItemEntities);

        /*
        twilio 메세지 전송
         */
        List<TwilioSmsRequestDto> twilioSmsRequestDtos = new ArrayList<>();

        StringBuilder smsMessage = new StringBuilder();
        smsMessage.append("[캠핑 렌탈]\n\n");
        smsMessage.append("신규 주문이 있습니다.\n\n");
        smsMessage.append("주문자 성함 : " + orderInfoDto.getOrderer() + "\n");
        smsMessage.append("주문자 전화번호 : " + orderInfoDto.getOrdererPhoneNumber() + "\n\n");
        smsMessage.append("조회 링크 바로가기 : " + "http://www.multranslator.com/search/order");

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

    @Transactional(readOnly = true)
    public Object searchList() {
        List<OrderVo> orderVos = new ArrayList<>();

        List<OrderInfoEntity> orderInfoEntities = orderInfoService.searchList();
        List<UUID> orderInfoIds = orderInfoEntities.stream().map(r -> {
            return r.getId();
        }).collect(Collectors.toList());
        List<OrderItemEntity> orderItemEntities = orderItemService.searchListByOrderInfoIds(orderInfoIds);

        orderInfoEntities.stream().forEach(orderInfo -> {
            OrderVo orderVo = new OrderVo();

            OrderInfoVo orderInfoVo = OrderInfoVo.toVo(orderInfo);
            List<OrderItemVo> orderItemVos = new ArrayList<>();

            orderItemEntities.stream().forEach(orderItem -> {
                if (orderInfo.getId().equals(orderItem.getOrderInfoId())) {
                    OrderItemVo orderItemVo = OrderItemVo.toVo(orderItem);
                    orderItemVos.add(orderItemVo);
                }
            });

            orderVo.setOrderInfo(orderInfoVo);
            orderVo.setOrderItems(orderItemVos);

            orderVos.add(orderVo);
        });

        return orderVos;
    }
}
