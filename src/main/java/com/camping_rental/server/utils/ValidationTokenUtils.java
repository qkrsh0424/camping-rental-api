package com.camping_rental.server.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;


@Slf4j
@Component
public class ValidationTokenUtils {

    private static String JWT_PHONE_VALIDATION_SECRET;
    private static String JWT_EMAIL_VALIDATION_SECRET;

    @Value("${app.jwt.phone-validation.secret}")
    public void setPhoneAuthJwtSecret(String phoneAuthJwtSecret) {
        ValidationTokenUtils.JWT_PHONE_VALIDATION_SECRET = phoneAuthJwtSecret;
    }

    @Value("${app.jwt.email-validation.secret}")
    public void setEmailAuthJwtSecret(String emailValidationSecret) {
        ValidationTokenUtils.JWT_EMAIL_VALIDATION_SECRET = emailValidationSecret;
    }

    public static String getJwtEmailValidationSecret(){
        return JWT_EMAIL_VALIDATION_SECRET;
    }

    public static String getJwtPhoneValidationSecret(){
        return JWT_PHONE_VALIDATION_SECRET;
    }

    public static String generatePhoneValidationNumberJwtToken(String authNumber, String phoneNumber) {
        String secret = phoneNumber + authNumber + JWT_PHONE_VALIDATION_SECRET;

        String jwt = CustomJwtUtils.generateJwtToken(
                "PHONE_VALIDATION_JWT",
                CustomJwtUtils.PHONE_VALIDATION_TOKEN_JWT_EXPIRATION,
                secret
        );

        return jwt;
    }

    public static String generateEmailValidationNumberJwtToken(String authNumber, String email) {
        String secret = email + authNumber + JWT_EMAIL_VALIDATION_SECRET;

        String jwt = CustomJwtUtils.generateJwtToken(
                "EMAIL_VALIDATION_JWT",
                CustomJwtUtils.EMAIL_VALIDATION_TOKEN_JWT_EXPIRATION,
                secret
        );

        return jwt;
    }

    public static String generate6DigitValidationCode(){
        String validationNum = String.valueOf((int) (Math.random() * 900000) + 100000);
        return validationNum;
    }

    public static void clearPhoneValidationCodeTokenCookie(HttpServletResponse response){
        ResponseCookie phoneValidationTokenCookie = ResponseCookie.from(CustomCookieUtils.COOKIE_NAME_PHONE_VALIDATION_TOKEN, null)
                .domain(CustomCookieUtils.COOKIE_DOMAIN)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, phoneValidationTokenCookie.toString());
    }
}