package com.camping_rental.server.domain.naver.sens.strategy;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.naver.sens.dto.NaverCloudSensSmsSendDto;

import java.util.Map;

public class NaverCloudSensSmsRoom {
    public static class ValidationCode implements NaverCloudSensSmsRequestStrategy {

        @Override
        public NaverCloudSensSmsSendDto returnNaverCloudSensSmsSendDto(Map<String, Object> requestInfo) {
            String validationNum = null;
            String smsReceiverPhoneNumber = null;

            try{
                validationNum = requestInfo.get("validationNum").toString();
                smsReceiverPhoneNumber = requestInfo.get("smsReceiverPhoneNumber").toString();
            } catch (IllegalArgumentException | NullPointerException e){
                throw new NotMatchedFormatException("메세지 전송 실패");
            }

            String smsMessage = "[Campal | 캠핑 렌탈] 본인확인 인증번호 [" + validationNum + "] 입니다. \"타인 노출 금지\"";

            NaverCloudSensSmsSendDto naverCloudSensSmsSendDto = NaverCloudSensSmsSendDto.toDto(smsReceiverPhoneNumber,smsMessage);

            return naverCloudSensSmsSendDto;
        }
    }
}
