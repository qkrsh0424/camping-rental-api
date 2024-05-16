package com.camping_rental.server.domain.naver.sens.strategy;

import com.camping_rental.server.domain.naver.sens.dto.NaverCloudSensSmsSendDto;
import com.camping_rental.server.domain.twilio.dto.TwilioSmsRequestDto;

import java.util.Map;

public interface NaverCloudSensSmsRequestStrategy {
    NaverCloudSensSmsSendDto returnNaverCloudSensSmsSendDto(Map<String, Object> requestInfo);
}
