package com.camping_rental.server.domain.twilio.strategy;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.twilio.dto.TwilioSmsRequestDto;

import java.util.Map;

public class RoomSms {
    public static class ValidationCode implements TwilioSmsRequestStrategy{

        @Override
        public TwilioSmsRequestDto returnTwilioSmsRequestDto(Map<String, Object> requestInfo) {
            String validationNum = null;
            String smsReceiverPhoneNumber = null;

            try{
                validationNum = requestInfo.get("validationNum").toString();
                smsReceiverPhoneNumber = requestInfo.get("smsReceiverPhoneNumber").toString();
            } catch (IllegalArgumentException | NullPointerException e){
                throw new NotMatchedFormatException("메세지 전송 실패");
            }

            String smsMessage = "[Campal | 캠핑 렌탈] 본인확인 인증번호 [" + validationNum + "] 입니다. \"타인 노출 금지\"";

            TwilioSmsRequestDto twilioSmsRequestDto = TwilioSmsRequestDto.builder()
                    .phoneNumber(smsReceiverPhoneNumber)
                    .message(smsMessage)
                    .build();

            return twilioSmsRequestDto;
        }
    }
}
