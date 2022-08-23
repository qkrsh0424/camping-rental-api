package com.camping_rental.server.domain.validation.service;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.naver.email.service.NaverEmailService;
import com.camping_rental.server.domain.twilio.service.TwilioSmsService;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.domain.validation.strategy.EmailValidationContext;
import com.camping_rental.server.domain.validation.strategy.EmailValidationName;
import com.camping_rental.server.domain.validation.strategy.PhoneValidationContext;
import com.camping_rental.server.domain.validation.strategy.PhoneValidationName;
import com.camping_rental.server.utils.DataFormatUtils;
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
    private final EmailValidationContext emailValidationContext;

    @Value("${app.email.validation.account}")
    private String EMAIL_SENDER_ACCOUNT;

    public void sendPhoneValidationCode(HttpServletResponse response, String validationType, String phoneNumber) {
        if (!DataFormatUtils.isPassPhoneNumberFormatValid(phoneNumber)) {
            throw new NotMatchedFormatException("휴대전화 형식이 정확하지 않습니다.");
        }
        phoneValidationContext.setStrategy(PhoneValidationName.fromString(validationType));
        phoneValidationContext.sendValidationCode(response, phoneNumber);
    }

    public void sendEmailValidationCode(HttpServletResponse response, String validationType, String email) {
        if (!DataFormatUtils.isPassEmailFormatValid(email)) {
            throw new NotMatchedFormatException("휴대전화 형식이 정확하지 않습니다.");
        }

        emailValidationContext.setStrategy(EmailValidationName.fromString(validationType));
        emailValidationContext.sendValidationCode(response, email);
    }
}
