package com.camping_rental.server.domain.order_info.service;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.order_info.dto.OrderInfoDto;
import com.camping_rental.server.domain.order_info.entity.OrderInfoEntity;
import com.camping_rental.server.domain.twilio.dto.TwilioSmsRequestDto;
import com.camping_rental.server.domain.twilio.service.TwilioSmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderInfoBusinessService {
    private final OrderInfoService orderInfoService;
    private final TwilioSmsService twilioSmsService;

    @Transactional
    public void changeStatus(UUID id, OrderInfoDto.OrderStatus orderStatusDto) {
        OrderInfoEntity orderInfoEntity = orderInfoService.searchOne(id);

        /*
        Dirty Checking Update
         */
        orderInfoEntity.setStatus(orderStatusDto.getSelectedStatus());

        /*
        메세지 전송
         */
        if (orderStatusDto.isSmsFlag()) {
            if(orderStatusDto.getSmsMessage().length() > 400){
                throw new NotMatchedFormatException("메세지의 길이가 너무 깁니다. (limit: 400)");
            }

            String phoneNumber = orderInfoEntity.getOrdererPhoneNumber();

            phoneNumber = phoneNumber.replaceAll("[^\\d]","");

            StringBuilder smsMessage = new StringBuilder();
            smsMessage.append("[Campal | 캠핑 렌탈]\n\n")
                    .append(orderStatusDto.getSmsMessage());

            List<TwilioSmsRequestDto> twilioSmsRequestDtos = new ArrayList<>();

            TwilioSmsRequestDto twilioSmsRequestDto = TwilioSmsRequestDto.builder()
                    .phoneNumber(phoneNumber)
                    .message(smsMessage.toString())
                    .build();

            twilioSmsRequestDtos.add(twilioSmsRequestDto);

            twilioSmsService.sendMultipleSms(twilioSmsRequestDtos);
        }
    }
}
