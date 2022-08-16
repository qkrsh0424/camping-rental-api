package com.camping_rental.server.domain.validation.service;

import com.camping_rental.server.domain.naver.email.service.NaverEmailService;
import com.camping_rental.server.domain.twilio.service.TwilioSmsService;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.domain.validation.strategy.PhoneValidationContext;
import com.camping_rental.server.domain.validation.strategy.PhoneValidationName;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class ValidationBusinessServiceV2 {
    private final UserService userService;
    private final TwilioSmsService twilioSmsService;
    private final NaverEmailService naverEmailService;
    private final PhoneValidationContext phoneValidationContext;

    @Value("${app.email.validation.account}")
    private String EMAIL_SENDER_ACCOUNT;

    public void sendPhoneValidationCode(HttpServletResponse response, String validationType, String phoneNumber) {
        phoneValidationContext.setStrategy(PhoneValidationName.fromString(validationType));
        phoneValidationContext.sendValidationCode(response, phoneNumber);
    }
}
