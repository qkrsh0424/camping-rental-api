package com.camping_rental.server.domain.twilio.strategy;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.twilio.dto.TwilioSmsRequestDto;

import java.util.Map;

public class RentalOrderInfoSms {
    public static class Orderer implements TwilioSmsRequestStrategy{

        @Override
        public TwilioSmsRequestDto returnTwilioSmsRequestDto(Map<String, Object> requestInfo) {
            String orderNumber = null;
            String smsReceiverPhoneNumber = null;

            try{
                orderNumber = requestInfo.get("orderNumber").toString();
                smsReceiverPhoneNumber = requestInfo.get("smsReceiverPhoneNumber").toString();
            } catch (IllegalArgumentException | NullPointerException e){
                throw new NotMatchedFormatException("알림문자 전송 실패");
            }

            StringBuilder ordererSmsMessage = new StringBuilder();
            ordererSmsMessage.append("[Campal | 캠핑 렌탈]\n")
                    .append("주문이 정상적으로 접수 되었습니다.\n\n")
                    .append("주문번호: " + orderNumber + "\n\n")
                    .append("주문 내역 조회 바로가기: https://www.campal.co.kr/search/order")
            ;

            TwilioSmsRequestDto twilioSmsRequestDto = TwilioSmsRequestDto.builder()
                    .phoneNumber(smsReceiverPhoneNumber)
                    .message(ordererSmsMessage.toString())
                    .build();

            return twilioSmsRequestDto;
        }
    }

    public static class Lender implements TwilioSmsRequestStrategy{

        @Override
        public TwilioSmsRequestDto returnTwilioSmsRequestDto(Map<String, Object> requestInfo) {
            String orderer = null;
            String ordererPhoneNumber = null;
            String smsReceiverPhoneNumber = null;

            try{
                orderer = requestInfo.get("orderer").toString();
                ordererPhoneNumber = requestInfo.get("ordererPhoneNumber").toString();
                smsReceiverPhoneNumber = requestInfo.get("smsReceiverPhoneNumber").toString();
            } catch (IllegalArgumentException | NullPointerException e){
                throw new NotMatchedFormatException("알림문자 전송 실패");
            }

            StringBuilder smsMessage = new StringBuilder();
            smsMessage.append("[Campal | 캠핑 렌탈]\n\n")
                    .append("신규 주문이 있습니다.\n\n")
                    .append("주문자 성함 : " + orderer + "\n")
                    .append("주문자 전화번호 : " + ordererPhoneNumber + "\n\n")
                    .append("조회 링크 바로가기 : " + "https://www.campal.co.kr/myadmin")
            ;

            TwilioSmsRequestDto twilioSmsRequestDto = TwilioSmsRequestDto.builder()
                    .phoneNumber(smsReceiverPhoneNumber)
                    .message(smsMessage.toString())
                    .build();

            return twilioSmsRequestDto;
        }
    }

    public static class Admin implements TwilioSmsRequestStrategy{
        @Override
        public TwilioSmsRequestDto returnTwilioSmsRequestDto(Map<String, Object> requestInfo) {
            String lender = null;
            String orderer = null;
            String ordererPhoneNumber = null;
            String smsReceiverPhoneNumber = null;

            try{
                lender = requestInfo.get("lender").toString();
                orderer = requestInfo.get("orderer").toString();
                ordererPhoneNumber = requestInfo.get("ordererPhoneNumber").toString();
                smsReceiverPhoneNumber = requestInfo.get("smsReceiverPhoneNumber").toString();
            } catch (IllegalArgumentException | NullPointerException e){
                throw new NotMatchedFormatException("알림문자 전송 실패");
            }

            StringBuilder smsMessage = new StringBuilder();
            smsMessage.append("[Campal | 캠핑 렌탈] for Admin\n\n")
                    .append(lender + " 님에게 신규 주문이 있습니다.\n\n")
                    .append("주문자 성함 : " + orderer + "\n")
                    .append("주문자 전화번호 : " + ordererPhoneNumber + "\n\n")
            ;

            TwilioSmsRequestDto twilioSmsRequestDto = TwilioSmsRequestDto.builder()
                    .phoneNumber(smsReceiverPhoneNumber)
                    .message(smsMessage.toString())
                    .build();

            return twilioSmsRequestDto;
        }
    }
}
