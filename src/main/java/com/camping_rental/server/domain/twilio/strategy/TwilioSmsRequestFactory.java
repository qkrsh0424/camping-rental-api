package com.camping_rental.server.domain.twilio.strategy;

import com.camping_rental.server.domain.twilio.dto.TwilioSmsRequestDto;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Setter
public class TwilioSmsRequestFactory {
    private TwilioSmsRequestStrategy twilioSmsRequestStrategy;

    public TwilioSmsRequestDto make(Map<String, Object> requestInfo){
        if(twilioSmsRequestStrategy == null){
            return null;
        }

        return twilioSmsRequestStrategy.returnTwilioSmsRequestDto(requestInfo);
    }
}
