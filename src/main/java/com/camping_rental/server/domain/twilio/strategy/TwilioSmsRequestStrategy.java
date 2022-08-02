package com.camping_rental.server.domain.twilio.strategy;

import com.camping_rental.server.domain.twilio.dto.TwilioSmsRequestDto;

import java.util.Map;

public interface TwilioSmsRequestStrategy {
    TwilioSmsRequestDto returnTwilioSmsRequestDto(Map<String, Object> requestInfo);
}
