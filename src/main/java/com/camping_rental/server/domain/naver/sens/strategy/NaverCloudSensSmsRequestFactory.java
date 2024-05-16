package com.camping_rental.server.domain.naver.sens.strategy;

import com.camping_rental.server.domain.naver.sens.dto.NaverCloudSensSmsSendDto;
import com.camping_rental.server.domain.twilio.dto.TwilioSmsRequestDto;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
public class NaverCloudSensSmsRequestFactory {
    private NaverCloudSensSmsRequestStrategy NaverCloudSensSmsRequestStrategy;

    public NaverCloudSensSmsSendDto make(Map<String, Object> requestInfo){
        if(NaverCloudSensSmsRequestStrategy == null){
            return null;
        }

        return NaverCloudSensSmsRequestStrategy.returnNaverCloudSensSmsSendDto(requestInfo);
    }
}
