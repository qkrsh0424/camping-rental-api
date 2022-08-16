package com.camping_rental.server.domain.validation.strategy;

import com.camping_rental.server.domain.twilio.dto.TwilioSmsRequestDto;
import com.camping_rental.server.domain.twilio.service.TwilioSmsService;
import com.camping_rental.server.domain.user.entity.UserEntity;
import com.camping_rental.server.domain.user.enums.UserLoginTypeEnum;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.utils.CustomCookieUtils;
import com.camping_rental.server.utils.ValidationTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

public class PhoneValidationBehavior {

    @RequiredArgsConstructor
    @Component
    public static class FindUsername implements PhoneValidaitonStrategy{
        private final UserService userService;
        private final TwilioSmsService twilioSmsService;

        @Override
        public PhoneValidationName getStrategyName() {
            return PhoneValidationName.ForFindUsername;
        }

        @Override
        public void sendValidationCode(HttpServletResponse response, String phoneNumber) {
            UserEntity userEntity = userService.searchByPhoneNumberAndLoginType(phoneNumber, UserLoginTypeEnum.LOCAL.getValue());
            if(userEntity == null){
                return;
            }

            String validationNum = ValidationTokenUtils.generate6DigitValidationCode();
            String validationNumToken = ValidationTokenUtils.generatePhoneValidationNumberJwtToken(validationNum, phoneNumber);

            /*
            인증번호 토큰을 cookie로 내보내준다.
             */
            ResponseCookie phoneAuthToken = ResponseCookie.from(CustomCookieUtils.COOKIE_NAME_PHONE_VALIDATION_TOKEN, validationNumToken)
                    .secure(CustomCookieUtils.SECURE)
                    .domain(CustomCookieUtils.COOKIE_DOMAIN)
                    .httpOnly(true)
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(CustomCookieUtils.PHONE_VALIDATION_COOKIE_EXPIRATION)
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, phoneAuthToken.toString());

            /*
            인증 코드를 담은 smsMessage를 트윌리오를 통해 전송한다.
             */
            String smsMessage = "[Campal | 캠핑 렌탈] 본인확인 인증번호 [" + validationNum + "] 입니다.\n\"타인 노출 금지\"";

            TwilioSmsRequestDto smsRequestDto = TwilioSmsRequestDto.builder()
                    .phoneNumber(phoneNumber)
                    .message(smsMessage)
                    .build();

            twilioSmsService.sendSmsAsync(smsRequestDto);
        }
    }

    @RequiredArgsConstructor
    @Component
    public static class FindPassword implements PhoneValidaitonStrategy{
        private final UserService userService;
        private final TwilioSmsService twilioSmsService;

        @Override
        public PhoneValidationName getStrategyName() {
            return PhoneValidationName.ForFindPassword;
        }

        @Override
        public void sendValidationCode(HttpServletResponse response, String phoneNumber) {
            UserEntity userEntity = userService.searchByPhoneNumberAndLoginType(phoneNumber, UserLoginTypeEnum.LOCAL.getValue());
            if(userEntity == null){
                return;
            }

            String validationNum = ValidationTokenUtils.generate6DigitValidationCode();
            String validationNumToken = ValidationTokenUtils.generatePhoneValidationNumberJwtToken(validationNum, phoneNumber);

            /*
            인증번호 토큰을 cookie로 내보내준다.
             */
            ResponseCookie phoneAuthToken = ResponseCookie.from(CustomCookieUtils.COOKIE_NAME_PHONE_VALIDATION_TOKEN, validationNumToken)
                    .secure(CustomCookieUtils.SECURE)
                    .domain(CustomCookieUtils.COOKIE_DOMAIN)
                    .httpOnly(true)
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(CustomCookieUtils.PHONE_VALIDATION_COOKIE_EXPIRATION)
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, phoneAuthToken.toString());

            /*
            인증 코드를 담은 smsMessage를 트윌리오를 통해 전송한다.
             */
            String smsMessage = "[Campal | 캠핑 렌탈] 본인확인 인증번호 [" + validationNum + "] 입니다. \"타인 노출 금지\"";

            TwilioSmsRequestDto smsRequestDto = TwilioSmsRequestDto.builder()
                    .phoneNumber(phoneNumber)
                    .message(smsMessage)
                    .build();

            twilioSmsService.sendSmsAsync(smsRequestDto);
        }
    }
}
