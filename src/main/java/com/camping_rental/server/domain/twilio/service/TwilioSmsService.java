package com.camping_rental.server.domain.twilio.service;

import com.camping_rental.server.config.twilio.TwilioSmsConfiguration;
import com.camping_rental.server.domain.twilio.dto.TwilioSmsRequestDto;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TwilioSmsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwilioSmsService.class);
    private final TwilioSmsConfiguration twilioSmsConfiguration;

    public void sendSms(TwilioSmsRequestDto twilioSmsRequestDto) {
        PhoneNumber to = new PhoneNumber(twilioSmsRequestDto.getFullPhoneNumber());
        PhoneNumber from = new PhoneNumber(twilioSmsConfiguration.getTrialNumber());
        String message = twilioSmsRequestDto.getMessage();

        MessageCreator creator = Message.creator(to, from, message);
        creator.create();
        LOGGER.info("Send sms {}", twilioSmsRequestDto);
    }

    public void sendSmsAsync(TwilioSmsRequestDto twilioSmsRequestDto) {
        PhoneNumber to = new PhoneNumber(twilioSmsRequestDto.getFullPhoneNumber());
        PhoneNumber from = new PhoneNumber(twilioSmsConfiguration.getTrialNumber());
        String message = twilioSmsRequestDto.getMessage();

        MessageCreator creator = Message.creator(to, from, message);
        creator.createAsync();
        LOGGER.info("Send sms {}", twilioSmsRequestDto);
    }

    public void sendMultipleSms(List<TwilioSmsRequestDto> twilioSmsRequestDtos){
        if(twilioSmsRequestDtos.size() <= 0){
            return;
        }
        twilioSmsRequestDtos.forEach(this::sendSmsAsync);
    }
}
