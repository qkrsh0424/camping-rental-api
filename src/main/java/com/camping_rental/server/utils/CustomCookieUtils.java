package com.camping_rental.server.utils;

import org.springframework.beans.factory.annotation.Value;

public class CustomCookieUtils {
    public static String COOKIE_DOMAIN;

    public final static Integer JWT_TOKEN_COOKIE_EXPIRATION = 5 * 24 * 60 * 60; // seconds - 5일
    public final static Integer CSRF_TOKEN_COOKIE_EXPIRATION = 5; // seconds - 5s

    public final static Integer PHONE_VALIDATION_COOKIE_EXPIRATION = 30 * 60; // seconds - 30분
    public final static Integer PHONE_VALIDATION_VF_COOKIE_EXPIRATION = 30 * 60; // seconds - 30분

    public final static Integer EMAIL_VALIDATION_COOKIE_EXPIRATION = 5 * 60; // seconds - 5분
    public final static Integer EMAIL_VALIDATION_VF_COOKIE_EXPIRATION = 10 * 60; // seconds - 10분

    public final static boolean SECURE = false; // PROD : true | DEV : false

    public final static String COOKIE_NAME_PHONE_VALIDATION_TOKEN = "cp_phone_validation_token";

    @Value("${app.cookie.domain}")
    public void setCookieDomain(String domain) {
        CustomCookieUtils.COOKIE_DOMAIN = domain;
    }
}
