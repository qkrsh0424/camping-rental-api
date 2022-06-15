package com.camping_rental.server.domain.validation.service;

import com.camping_rental.server.domain.twilio.dto.TwilioSmsRequestDto;
import com.camping_rental.server.domain.twilio.service.TwilioSmsService;
import com.camping_rental.server.domain.user.entity.UserEntity;
import com.camping_rental.server.domain.user.enums.UserLoginTypeEnum;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.utils.CustomCookieUtils;
import com.camping_rental.server.utils.DataFormatUtils;
import com.camping_rental.server.utils.ValidationTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class ValidationBusinessService {
    private final UserService userService;
    private final TwilioSmsService twilioSmsService;

    /*
    입력받은 phoneNumber의 포맷 형식을 확인한다.
    입력받은 phoneNumber 와 유저 login_type이 local인 경우의 UserEntity를 찾는다. 만약에 해당 데이터가 있다면 return
    인증번호와 인증번호 토큰을 만든다.
    인증번호 토큰을 cookie로 내보내준다.
    인증 코드를 담은 smsMessage를 트윌리오를 통해 전송한다.
     */
    public void sendPhoneAuthCode(HttpServletResponse response, String phoneNumber) {

        /*
        입력받은 phoneNumber의 포맷 형식을 확인한다.
         */
        DataFormatUtils.checkPhoneNumberFormat(phoneNumber);

        /*
        입력받은 phoneNumber 와 유저 login_type이 local인 경우의 UserEntity를 찾는다. 만약에 해당 데이터가 있다면 return
         */
        UserEntity userEntity = userService.searchByPhoneNumberAndLoginType(phoneNumber, UserLoginTypeEnum.LOCAL.getValue());
        if(userEntity != null){
            return;
        }

        /*
        인증번호와 인증번호 토큰을 만든다.
         */
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
        String smsMessage = "[Campal | 캠핑 렌탈] 본인확인 인증번호[" + validationNum + "]입니다. \"타인 노출 금지\"";

        TwilioSmsRequestDto smsRequestDto = TwilioSmsRequestDto.builder()
                .phoneNumber(phoneNumber)
                .message(smsMessage)
                .build();

        twilioSmsService.sendSmsAsync(smsRequestDto);
    }
}
