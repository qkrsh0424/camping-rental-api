package com.camping_rental.server.domain.validation.strategy;

import com.camping_rental.server.domain.naver.email.dto.NaverEmailRequestDto;
import com.camping_rental.server.domain.naver.email.service.NaverEmailService;
import com.camping_rental.server.domain.twilio.dto.TwilioSmsRequestDto;
import com.camping_rental.server.domain.twilio.service.TwilioSmsService;
import com.camping_rental.server.domain.user.entity.UserEntity;
import com.camping_rental.server.domain.user.enums.UserLoginTypeEnum;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.utils.CustomCookieUtils;
import com.camping_rental.server.utils.ValidationTokenUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmailValidationBehavior {
    @RequiredArgsConstructor
    @Component
    public static class Modify implements EmailValidationStrategy{
        private final UserService userService;
        private final NaverEmailService naverEmailService;
        @Value("${app.email.validation.account}")
        private String EMAIL_SENDER_ACCOUNT;

        @Override
        public EmailValidationName getStrategyName() {
            return EmailValidationName.ForModify;
        }

        @Override
        public void sendValidationCode(HttpServletResponse response, String email) {
            UserEntity userEntity = userService.searchByEmailAndLoginTypeElseNull(email, UserLoginTypeEnum.LOCAL.getValue());

            if(userEntity != null){
                return;
            }

            String validationNum = ValidationTokenUtils.generate6DigitValidationCode();
            String validationNumToken = ValidationTokenUtils.generateEmailValidationNumberJwtToken(validationNum, email);

            /*
            인증번호 토큰을 cookie로 내보내준다.
             */
            ResponseCookie emailValidationToken = ResponseCookie.from(CustomCookieUtils.COOKIE_NAME_EMAIL_VALIDATION_TOKEN, validationNumToken)
                    .secure(CustomCookieUtils.SECURE)
                    .domain(CustomCookieUtils.COOKIE_DOMAIN)
                    .httpOnly(true)
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(CustomCookieUtils.EMAIL_VALIDATION_COOKIE_EXPIRATION)
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, emailValidationToken.toString());

            String sendMessage = "";

            try {
                File templateFile = new File("src/main/resources/static/emailAuthTemplate.html");
                FileInputStream emailTemplate = new FileInputStream(templateFile);
                sendMessage = IOUtils.toString(emailTemplate, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            sendMessage = sendMessage.replaceFirst("------", validationNum);

            List<NaverEmailRequestDto.ReceipientForRequest> receipients = new ArrayList<>();
            NaverEmailRequestDto.ReceipientForRequest receipient = NaverEmailRequestDto.ReceipientForRequest.builder()
                    .address(email)
                    .type("R")
                    .build();
            receipients.add(receipient);

            NaverEmailRequestDto.Send mailRequestDto = NaverEmailRequestDto.Send.builder()
                    .senderAddress(EMAIL_SENDER_ACCOUNT)
                    .senderName("Campal | 캠핑 렌탈")
                    .title("[캠팔] 이메일 인증")
                    .body(sendMessage)
                    .recipients(receipients)
                    .build();

            naverEmailService.sendEmail(mailRequestDto);
        }
    }
}
